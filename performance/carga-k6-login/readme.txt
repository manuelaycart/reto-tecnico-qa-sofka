===============================================================================
 RETO TÉCNICO QA - EJERCICIO DE PERFORMANCE / EJERCICIO 1
 Prueba de carga del servicio de login con k6
 Autor: Manuel Aycart
===============================================================================

1. OBJETIVO
-----------
Ejecutar una prueba de carga sobre el login de FakeStoreAPI:
  POST https://fakestoreapi.com/auth/login
  Content-Type: application/json
  { "username": "<user>", "password": "<passwd>" }

  - Credenciales parametrizadas desde data/usuarios.csv
  - Escenario de al menos 20 TPS
  - Validaciones: tiempo de respuesta máximo 1,5 s y tasa de error menor al 3 %


2. TECNOLOGÍAS Y VERSIONES
--------------------------
  k6 ....................... 2.3.0 (compatible con k6 0.49 o superior)
  papaparse ................ 5.1.1 (lectura del CSV, se importa desde jslib.k6.io)
  Sistema operativo ........ Windows, macOS o Linux

  No requiere Node.js ni otras dependencias: k6 es un binario único.


3. INSTALACIÓN DE k6
--------------------
  macOS ........ brew install k6
  Windows ...... winget install k6 --source winget     (o: choco install k6)
  Linux ........ ver https://grafana.com/docs/k6/latest/set-up/install-k6/
  Docker ....... docker pull grafana/k6:2.3.0

  Verificar la instalación: k6 version


4. PASOS DE EJECUCIÓN
---------------------
  1. Clonar el repositorio:
       git clone https://github.com/manuelaycart/reto-tecnico-qa-sofka.git

  2. Entrar a la carpeta del ejercicio:
       cd reto-tecnico-qa-sofka/performance/carga-k6-login

  3. Ejecutar la prueba (dura aproximadamente 2 min 30 s):

       Linux / macOS:    ./run-load-test.sh
       Windows:          powershell -ExecutionPolicy Bypass -File .\run-load-test.ps1

     Equivalente sin los scripts (solo resumen en consola):
       k6 run scripts/login-load-test.js

     Con Docker (desde la carpeta del ejercicio):
       docker run --rm -i -v "${PWD}:/work" -w /work grafana/k6:2.3.0 run scripts/login-load-test.js

  4. Revisar los reportes generados en la carpeta reports/:
       reports/summary.txt     resumen de fin de prueba (thresholds y métricas)
       reports/summary.json    métricas agregadas en JSON
       reports/dashboard.html  reporte HTML con gráficas de TPS, latencia y VUs

  El comando termina con código 0 si se cumplen todos los criterios y con
  código 99 si algún threshold falla.


5. PARÁMETROS (VARIABLES DE ENTORNO OPCIONALES)
-----------------------------------------------
  BASE_URL          URL del servicio ...................... https://fakestoreapi.com
  TARGET_TPS        TPS de la fase sostenida .............. 30
  RAMP_UP           duración de la rampa de subida ........ 20s
  STEADY_DURATION   duración de la fase sostenida ......... 2m
  RAMP_DOWN         duración de la rampa de bajada ........ 10s

  Ejemplo:  TARGET_TPS=40 STEADY_DURATION=5m ./run-load-test.sh
  Windows:  $env:TARGET_TPS="40"; .\run-load-test.ps1


6. DISEÑO DEL ESCENARIO
-----------------------
  - Executor ramping-arrival-rate (modelo abierto): k6 inicia N iteraciones por
    segundo sin depender del tiempo de respuesta, así los TPS inyectados son los
    configurados. Cada iteración es un login, entonces iteraciones/s = TPS.
  - Perfil: 20 s de subida hasta 30 TPS, 2 min sostenidos a 30 TPS y 10 s de
    bajada. Se usaron 30 TPS (50 % por encima del mínimo) para que el promedio de
    toda la prueba, rampas incluidas, quede por encima de 20 TPS.
  - Datos: el CSV se carga una sola vez en un SharedArray y cada iteración toma
    el siguiente usuario (round-robin), así la carga se reparte por igual entre
    las 5 credenciales.
  - Smoke test en setup(): valida conectividad y credenciales antes de generar
    carga; si falla, la prueba se aborta.

  Criterios de aceptación (thresholds)
    http_req_duration{transaccion:login}  p(95) < 1500 ms
    http_req_failed{transaccion:login}    rate < 3 %
    login_errors                          rate < 3 %   (status distinto de 201,
                                                        sin token JWT o > 1,5 s)
    http_reqs{transaccion:login}          rate >= 20 TPS
    Corte de seguridad: si los errores superan el 20 % después de 30 s, la
    prueba se detiene para no seguir cargando un servicio caído.

  Validaciones por petición (checks)
    - status 201 (login exitoso)
    - la respuesta contiene un token con formato JWT
    - tiempo de respuesta <= 1,5 s


7. ESTRUCTURA
-------------
  scripts/login-load-test.js   script de la prueba
  data/usuarios.csv            credenciales parametrizadas
  run-load-test.sh / .ps1      ejecución con exportación de reportes
  evidencias/                  reportes de una ejecución real (summary.txt,
                               summary.json y dashboard.html)


8. INTEGRACIÓN CONTINUA
-----------------------
.github/workflows/performance-k6.yml ejecuta la prueba en GitHub Actions en cada
push que modifique esta carpeta. También se puede lanzar manualmente desde la
pestaña Actions eligiendo los TPS y la duración. Los reportes quedan como
artefacto de la ejecución.
