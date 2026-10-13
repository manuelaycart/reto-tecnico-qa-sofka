/**
 * Prueba de carga - Servicio de login de FakeStoreAPI
 * ---------------------------------------------------------------------------
 * Endpoint : POST https://fakestoreapi.com/auth/login
 * Datos    : credenciales parametrizadas desde ../data/usuarios.csv
 * Modelo   : abierto (executor ramping-arrival-rate). k6 inicia N iteraciones
 *            por segundo sin importar cuanto tarde el servicio, por lo que los
 *            TPS inyectados no dependen del tiempo de respuesta. Cada iteracion
 *            es exactamente un login, entonces iteraciones/s = TPS.
 *
 * Criterios de aceptacion (thresholds):
 *   1. Tiempo de respuesta maximo 1,5 s  -> p(95) < 1500 ms y cada peticion
 *      que supere 1,5 s cuenta como transaccion fallida (login_errors).
 *   2. Tasa de error < 3 % del total de peticiones (http_req_failed y
 *      login_errors).
 *   3. Throughput promedio de la prueba >= 20 TPS.
 *
 * Variables de entorno opcionales:
 *   BASE_URL         URL del servicio                (https://fakestoreapi.com)
 *   TARGET_TPS       TPS de la fase sostenida        (30)
 *   RAMP_UP          duracion de la rampa de subida  (20s)
 *   STEADY_DURATION  duracion de la fase sostenida   (2m)
 *   RAMP_DOWN        duracion de la rampa de bajada  (10s)
 */
import http from 'k6/http';
import { check } from 'k6';
import { SharedArray } from 'k6/data';
import exec from 'k6/execution';
import { Rate } from 'k6/metrics';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';

const BASE_URL = __ENV.BASE_URL || 'https://fakestoreapi.com';
const TARGET_TPS = parseInt(__ENV.TARGET_TPS || '30', 10);
const RAMP_UP = __ENV.RAMP_UP || '20s';
const STEADY_DURATION = __ENV.STEADY_DURATION || '2m';
const RAMP_DOWN = __ENV.RAMP_DOWN || '10s';

// Criterios definidos en el enunciado del ejercicio
const MAX_RESPONSE_TIME_MS = 1500;
const MAX_ERROR_RATE = 0.03;
const MIN_TPS = 20;

const LOGIN_REQUEST_NAME = 'POST /auth/login';
const JWT_PATTERN = /^[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+$/;

// El CSV se lee una sola vez y se comparte en modo solo lectura entre los VUs.
const users = new SharedArray('usuarios', function () {
  const csv = papaparse.parse(open('../data/usuarios.csv'), {
    header: true,
    skipEmptyLines: true,
  });
  return csv.data.map((row) => ({
    username: row.user.trim(),
    password: row.passwd.trim(),
  }));
});

// Transaccion fallida: status inesperado, sin token JWT o respuesta > 1,5 s.
const loginErrors = new Rate('login_errors');

export const options = {
  scenarios: {
    login: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 100,
      stages: [
        { duration: RAMP_UP, target: TARGET_TPS }, // calentamiento
        { duration: STEADY_DURATION, target: TARGET_TPS }, // carga sostenida
        { duration: RAMP_DOWN, target: 0 }, // descenso
      ],
      gracefulStop: '10s',
    },
  },
  thresholds: {
    'http_req_duration{transaccion:login}': [`p(95)<${MAX_RESPONSE_TIME_MS}`],
    'http_req_failed{transaccion:login}': [
      `rate<${MAX_ERROR_RATE}`,
      // Corte de seguridad: si el servicio se cae, no seguir enviando carga.
      { threshold: 'rate<0.20', abortOnFail: true, delayAbortEval: '30s' },
    ],
    login_errors: [`rate<${MAX_ERROR_RATE}`],
    'http_reqs{transaccion:login}': [`rate>=${MIN_TPS}`],
  },
  summaryTrendStats: ['avg', 'min', 'med', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

export function setup() {
  if (users.length === 0) {
    exec.test.abort('El archivo usuarios.csv no contiene registros');
  }

  // Smoke test: confirma conectividad y credenciales antes de generar carga.
  const res = login(users[0], 'smoke');
  if (!isSuccessfulLogin(res)) {
    exec.test.abort(
      `Smoke test fallido: status=${res.status} error=${res.error || '-'} ` +
        `body=${String(res.body).slice(0, 200)}`,
    );
  }

  console.log(
    `Smoke test OK (status=${res.status}, ${Math.round(res.timings.duration)} ms). ` +
      `Inicio de la carga: ${users.length} usuarios en CSV, ${TARGET_TPS} TPS sostenidos ` +
      `por ${STEADY_DURATION} contra ${BASE_URL}`,
  );
}

export default function () {
  // Round-robin global: cada iteracion toma el siguiente usuario del CSV,
  // de modo que la carga se reparte en partes iguales entre las credenciales.
  const user = users[exec.scenario.iterationInTest % users.length];
  const res = login(user, 'login');

  const passed = check(res, {
    'status 201 (login exitoso)': (r) => isSuccessfulLogin(r),
    'respuesta contiene un token JWT': (r) => JWT_PATTERN.test(tokenOf(r)),
    'tiempo de respuesta <= 1,5 s': (r) => r.timings.duration <= MAX_RESPONSE_TIME_MS,
  });

  loginErrors.add(!passed, { usuario: user.username });

  if (!passed) {
    logFailure(user, res);
  }
}

function login(user, transaccion) {
  const payload = JSON.stringify({ username: user.username, password: user.password });

  return http.post(`${BASE_URL}/auth/login`, payload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { name: LOGIN_REQUEST_NAME, transaccion: transaccion, usuario: user.username },
    timeout: '60s', // equivalente al --max-time 60 del cURL entregado
  });
}

// El servicio responde 201 Created con un login valido (verificado en el smoke test).
function isSuccessfulLogin(res) {
  return res.status === 201;
}

function tokenOf(res) {
  try {
    const token = res.json('token');
    return typeof token === 'string' ? token : '';
  } catch (e) {
    return '';
  }
}

// Se registran como maximo 3 fallos por VU para no inundar la consola.
let failuresLogged = 0;

function logFailure(user, res) {
  if (failuresLogged >= 3) {
    return;
  }
  failuresLogged++;
  console.warn(
    `Login fallido | VU=${exec.vu.idInTest} usuario=${user.username} status=${res.status} ` +
      `tiempo=${Math.round(res.timings.duration)}ms error=${res.error || '-'} ` +
      `body=${String(res.body).slice(0, 120)}`,
  );
}
