@ignore
Feature: Paso reutilizable - limpieza de datos de prueba

  Se ejecuta en el afterScenario para no dejar usuarios de prueba en la API pública.
  No valida el status: si el escenario ya eliminó al usuario, la API responde 404 y está bien.

  Scenario: Eliminar el usuario "nombreUsuario" si todavía existe
    Given url baseUrl
    And path 'user', nombreUsuario
    When method delete
    * karate.log('Limpieza del usuario', nombreUsuario, '-> status', responseStatus)
