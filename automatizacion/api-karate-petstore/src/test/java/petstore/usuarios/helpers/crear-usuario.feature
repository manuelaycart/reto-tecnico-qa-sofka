@ignore
Feature: Paso reutilizable - crear un usuario

  Usado como precondición por otros escenarios.
  Entrada: variable "cuerpo" con el JSON del usuario a crear.

  Scenario: Crear el usuario recibido en "cuerpo"
    Given url baseUrl
    And path 'user'
    And request cuerpo
    When method post
    Then status 200
    And match response.message == cuerpo.id + ''
