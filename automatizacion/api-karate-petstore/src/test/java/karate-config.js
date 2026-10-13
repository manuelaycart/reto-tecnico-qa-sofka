function fn() {
  var env = karate.env || 'qa';
  karate.log('Ambiente de ejecucion (karate.env):', env);

  var config = {
    env: env,
    baseUrl: 'https://petstore.swagger.io/v2'
  };

  // Permite apuntar a otra instancia de PetStore: mvn test -DbaseUrl=http://localhost:8080/v2
  if (karate.properties['baseUrl']) {
    config.baseUrl = karate.properties['baseUrl'];
  }

  karate.configure('connectTimeout', 10000);
  karate.configure('readTimeout', 20000);

  // PetStore es una API publica compartida y no siempre refleja de inmediato
  // una escritura en la siguiente lectura (consistencia eventual). Las
  // consultas usan "retry until" con esta politica antes de dar el caso por fallido.
  karate.configure('retry', { count: 10, interval: 1500 });

  karate.configure('logPrettyRequest', true);
  karate.configure('logPrettyResponse', true);

  return config;
}
