@petstore @usuarios @e2e
Feature: Flujo E2E encadenado del recurso /user de PetStore

  Ejecuta los cinco casos del enunciado en orden y usa la salida de cada paso
  como entrada del siguiente:
  crear -> buscar -> actualizar nombre y correo -> buscar actualizado -> eliminar

  Background:
    * url baseUrl
    * def datos = call read('classpath:petstore/usuarios/data/generar-usuario.js')
    * def nombreUsuario = datos.username
    * def usuario = read('classpath:petstore/usuarios/data/usuario-request.json')
    * def esquemaUsuario = read('classpath:petstore/usuarios/schemas/usuario-schema.json')
    * def esquemaRespuesta = read('classpath:petstore/usuarios/schemas/respuesta-api-schema.json')
    * configure afterScenario = function(){ karate.call('classpath:petstore/usuarios/helpers/eliminar-usuario.feature', { nombreUsuario: nombreUsuario }) }

  Scenario: Ciclo de vida completo de un usuario
    # -------------------------------------------------------------------------
    # Caso 1 - Crear un usuario
    #   Entrada : usuario-request.json con id, username y email únicos
    #   Salida  : { code: 200, type: 'unknown', message: '<id del usuario>' }
    # -------------------------------------------------------------------------
    Given path 'user'
    And request usuario
    When method post
    Then status 200
    And match response == esquemaRespuesta
    And match response.message == datos.id + ''
    * def idCreado = response.message

    # -------------------------------------------------------------------------
    # Caso 2 - Buscar el usuario creado
    #   Entrada : username generado en el caso 1
    #   Salida  : el usuario con exactamente los datos enviados
    # -------------------------------------------------------------------------
    Given path 'user', nombreUsuario
    And retry until responseStatus == 200
    When method get
    Then status 200
    And match response == esquemaUsuario
    And match response == usuario
    * def usuarioCreado = response

    # -------------------------------------------------------------------------
    # Caso 3 - Actualizar el nombre y el correo del usuario
    #   Entrada : usuario del caso 2 con firstName y email nuevos
    #   Salida  : { code: 200, type: 'unknown', message: '<id del usuario>' }
    # -------------------------------------------------------------------------
    * copy usuarioActualizado = usuarioCreado
    * set usuarioActualizado.firstName = 'Laura Sofia'
    * set usuarioActualizado.email = 'laura.sofia.' + idCreado + '@example.com'
    Given path 'user', nombreUsuario
    And request usuarioActualizado
    When method put
    Then status 200
    And match response == esquemaRespuesta
    And match response.message == idCreado

    # -------------------------------------------------------------------------
    # Caso 4 - Buscar el usuario actualizado
    #   Salida : firstName y email nuevos; el resto de campos sin cambios
    # -------------------------------------------------------------------------
    Given path 'user', nombreUsuario
    And retry until responseStatus == 200 && response.email == usuarioActualizado.email
    When method get
    Then status 200
    And match response == esquemaUsuario
    And match response == usuarioActualizado
    And match response.firstName != usuarioCreado.firstName
    And match response.email != usuarioCreado.email
    And match response.lastName == usuarioCreado.lastName
    * print 'Antes:', usuarioCreado.firstName, '/', usuarioCreado.email, '| Después:', response.firstName, '/', response.email

    # -------------------------------------------------------------------------
    # Caso 5 - Eliminar el usuario
    #   Salida : { code: 200, type: 'unknown', message: '<username>' } y luego
    #            404 "User not found" al volver a consultarlo
    # -------------------------------------------------------------------------
    Given path 'user', nombreUsuario
    And retry until responseStatus == 200
    When method delete
    Then status 200
    And match response.message == nombreUsuario

    Given path 'user', nombreUsuario
    And retry until responseStatus == 404
    When method get
    Then status 404
    And match response.message == 'User not found'
