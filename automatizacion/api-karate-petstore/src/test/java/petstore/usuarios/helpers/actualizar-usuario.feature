@ignore
Feature: Paso reutilizable - actualizar un usuario

  Entradas: "nombreUsuario" (username a modificar) y "cuerpo" (JSON con los nuevos datos).

  Scenario: Actualizar el usuario "nombreUsuario" con los datos de "cuerpo"
    Given url baseUrl
    And path 'user', nombreUsuario
    And request cuerpo
    When method put
    Then status 200
