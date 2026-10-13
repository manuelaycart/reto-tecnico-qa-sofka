===============================================================================
 RETO TÉCNICO QA - PRUEBA TÉCNICA / EJERCICIO 1
 Prueba E2E automatizada: compra como invitado en OpenCart
 Autor: Manuel Aycart
===============================================================================

1. OBJETIVO
-----------
Automatizar el flujo de compra en http://opencart.abstracta.us/ :
  - Agregar dos productos al carrito
  - Visualizar el carrito
  - Completar el checkout como invitado ("Guest Checkout")
  - Finalizar la compra hasta la confirmación "Your order has been placed!"


2. TECNOLOGÍAS Y VERSIONES
--------------------------
  Java (JDK) ............... 17 o superior (ejecutado con Eclipse Temurin 21)
  Apache Maven ............. 3.9 o superior
  Google Chrome ............ última versión estable (ejecutado con Chrome 154)
  Serenity BDD ............. 5.3.8 (patrón Screenplay)
  Cucumber ................. 7.34.2 (Gherkin en español)
  JUnit Platform ........... 6.0.3
  Selenium ................. 4.41.0 (incluido en Serenity; el driver de Chrome
                             lo descarga automáticamente Selenium Manager)

No hace falta instalar ChromeDriver a mano.


3. REQUISITOS PREVIOS
---------------------
  a) JDK 17+ instalado y JAVA_HOME configurado.      Verificar: java -version
  b) Maven 3.9+ en el PATH.                          Verificar: mvn -version
  c) Google Chrome instalado.
  d) Conexión a internet (sitio de pruebas, dependencias de Maven y driver).


4. PASOS DE EJECUCIÓN
---------------------
  1. Clonar el repositorio:
       git clone https://github.com/manuelaycart/reto-tecnico-qa-sofka.git

  2. Entrar a la carpeta del ejercicio:
       cd reto-tecnico-qa-sofka/automatizacion/e2e-serenity-opencart

  3. Ejecutar la prueba (la primera vez Maven descarga las dependencias):
       mvn clean verify

     Por defecto el navegador corre en modo headless. Para verlo en pantalla:
       mvn clean verify -Dheadless.mode=false

  4. Abrir el reporte:
       target/site/serenity/index.html            (reporte completo)
       target/site/serenity/serenity-summary.html (resumen de una página)

  Opciones adicionales:
     - Ejecutar solo un tag ............ mvn clean verify -Dcucumber.filter.tags="@e2e"
     - Usar la versión HTTPS del sitio .. mvn clean verify -Denvironment=https

  El resultado esperado en consola es "Tests run: 1, Failures: 0" y BUILD SUCCESS.


5. ESCENARIO AUTOMATIZADO
-------------------------
Archivo: src/test/resources/features/compra_invitado.feature

  Escenario: Compra exitosa de dos productos mediante Guest Checkout
    Dado que Laura ingresa a la tienda OpenCart
    Cuando agrega al carrito los productos: iPod Classic, HTC Touch HD
    Y revisa su carrito de compras
    Entonces debería ver en el carrito los productos agregados
    Cuando completa el checkout como invitado con los datos: (tabla clave/valor)
    Entonces debería ver el mensaje de confirmación "Your order has been placed!"

Los productos y los datos del cliente están en el feature: se pueden cambiar
sin tocar el código.


6. ESTRUCTURA DEL PROYECTO (PATRÓN SCREENPLAY)
----------------------------------------------
src/test/java/com/retoqa/opencart/
  runners/          OpenCartTestSuite: runner de Cucumber sobre JUnit Platform
  stepdefinitions/  Pasos del feature, hooks (OnStage) y parámetro {actor}
  tasks/            Tareas de negocio: abrir tienda, agregar productos, ver carrito,
                    checkout como invitado
  tasks/checkout/   Una tarea por cada paso del checkout (pasos 1, 2, 4, 5 y 6)
  interactions/     Interacciones propias: esperar URL, marcar casilla
  questions/        Productos del carrito, alerta de stock, mensaje de confirmación
  userinterfaces/   Localizadores (Targets) agrupados por página
  models/           Customer: datos del invitado construidos desde el feature
  utils/            Claves de memoria del actor y tiempos de espera
src/test/resources/
  features/         compra_invitado.feature
  serenity.conf     Navegador, capturas, URL base por ambiente
  junit-platform.properties, logback-test.xml


7. INTEGRACIÓN CONTINUA
-----------------------
.github/workflows/e2e-serenity.yml ejecuta la prueba en GitHub Actions en cada
push que modifique esta carpeta (o manualmente desde la pestaña Actions) y
publica el reporte de Serenity como artefacto descargable.


8. EVIDENCIAS
-------------
evidencias/README.md       capturas de cada paso de una ejecución exitosa
evidencias/capturas/       capturas de pantalla
evidencias/serenity-summary.html  reporte de una página de Serenity
