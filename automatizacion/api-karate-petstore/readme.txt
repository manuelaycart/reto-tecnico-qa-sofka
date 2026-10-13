===============================================================================
 RETO TÉCNICO QA - PRUEBA TÉCNICA / EJERCICIO 2
 Pruebas de servicios REST con Karate: usuarios de PetStore
 Autor: Manuel Aycart
===============================================================================

1. OBJETIVO
-----------
Probar el recurso /user de la API https://petstore.swagger.io identificando
entradas, salidas, validaciones y variables en cada caso:
  1. Crear un usuario                       POST   /v2/user
  2. Buscar el usuario creado               GET    /v2/user/{username}
  3. Actualizar el nombre y el correo       PUT    /v2/user/{username}
  4. Buscar el usuario actualizado          GET    /v2/user/{username}
  5. Eliminar el usuario                    DELETE /v2/user/{username}


2. TECNOLOGÍAS Y VERSIONES
--------------------------
  Java (JDK) ............... 17 o superior (ejecutado con Eclipse Temurin 21)
  Apache Maven ............. 3.9 o superior
  Karate ................... 1.5.2 (io.karatelabs:karate-junit5)
  JUnit .................... 5 (incluido en karate-junit5)


3. REQUISITOS PREVIOS
---------------------
  a) JDK 17+ instalado y JAVA_HOME configurado.      Verificar: java -version
  b) Maven 3.9+ en el PATH.                          Verificar: mvn -version
  c) Conexión a internet.


4. PASOS DE EJECUCIÓN
---------------------
  1. Clonar el repositorio:
       git clone https://github.com/manuelaycart/reto-tecnico-qa-sofka.git

  2. Entrar a la carpeta del ejercicio:
       cd reto-tecnico-qa-sofka/automatizacion/api-karate-petstore

  3. Ejecutar todas las pruebas:
       mvn clean test

  4. Abrir el reporte HTML:
       target/karate-reports/karate-summary.html

  El resultado esperado en consola es "scenarios: 7 | passed: 7 | failed: 0".

  Ejecutar un caso puntual por tag:
       mvn clean test -Dkarate.options="--tags @crear"
       (tags: @crear, @buscar, @actualizar, @buscar_actualizado, @eliminar,
        @negativo, @e2e)

  Apuntar a otra instancia de PetStore:
       mvn clean test -DbaseUrl=http://localhost:8080/v2


5. CASOS AUTOMATIZADOS
----------------------
src/test/java/petstore/usuarios/gestion-usuarios.feature
  Escenarios independientes. Cada uno genera datos únicos, prepara sus
  precondiciones con pasos reutilizables y elimina al final lo que creó.
    @crear               Crear un usuario
    @buscar              Buscar el usuario creado
    @actualizar          Actualizar el nombre y el correo del usuario
    @buscar_actualizado  Buscar el usuario actualizado
    @eliminar            Eliminar el usuario (y confirmar 404 al consultarlo)
    @negativo            Consultar un usuario inexistente retorna 404

src/test/java/petstore/usuarios/ciclo-vida-usuario.feature
    @e2e  Los cinco casos encadenados en un solo flujo: la salida de cada paso
          (id, usuario creado, datos actualizados) es la entrada del siguiente.

Entradas, salidas y variables por caso
  Caso 1  Entrada : usuario-request.json (id, username y email únicos)
          Salida  : { code: 200, type: "unknown", message: "<id>" }
          Variable: idCreado = response.message
  Caso 2  Entrada : username del caso 1
          Salida  : el usuario completo; se valida esquema y que sea igual al enviado
          Variable: usuarioCreado = response
  Caso 3  Entrada : usuarioCreado con firstName y email nuevos
          Salida  : { code: 200, type: "unknown", message: "<id>" }
          Variable: usuarioActualizado
  Caso 4  Entrada : username
          Salida  : firstName y email nuevos; el resto de campos sin cambios
  Caso 5  Entrada : username
          Salida  : { code: 200, type: "unknown", message: "<username>" } y luego
                    404 { code: 1, type: "error", message: "User not found" }


6. ESTRUCTURA DEL PROYECTO
--------------------------
src/test/java/
  karate-config.js                 URL base, timeouts y política de reintentos
  logback-test.xml                 Log en consola y en target/karate.log
  petstore/PetStoreRunnerTest.java Runner JUnit 5 (genera el reporte)
  petstore/usuarios/
    gestion-usuarios.feature       Casos independientes
    ciclo-vida-usuario.feature     Flujo E2E encadenado
    helpers/                       Pasos reutilizables (@ignore): crear,
                                   actualizar y eliminar usuario
    data/                          Generador de datos únicos y JSON del request
    schemas/                       Esquemas de respuesta (usuario y respuesta API)


7. INTEGRACIÓN CONTINUA
-----------------------
.github/workflows/api-karate.yml ejecuta las pruebas en GitHub Actions en cada
push que modifique esta carpeta (o manualmente) y publica el reporte como
artefacto.


8. EVIDENCIAS
-------------
evidencias/karate-reports/  reporte HTML de una ejecución exitosa.
                            Abrir evidencias/karate-reports/karate-summary.html
