@petstore @usuarios
Feature: Gestión de usuarios en la API PetStore (/v2/user)

  Como consumidor de la API PetStore
  Quiero crear, consultar, actualizar y eliminar usuarios
  Para validar el ciclo de vida del recurso /user

  Cada escenario es independiente: genera datos únicos, arma sus propias
  precondiciones con los pasos reutilizables de helpers/ y, al terminar,
  elimina el usuario que creó (afterScenario).

  Background:
    * url baseUrl
    * def datos = call read('classpath:petstore/usuarios/data/generar-usuario.js')
    * def nombreUsuario = datos.username
    * def usuario = read('classpath:petstore/usuarios/data/usuario-request.json')
    * def esquemaUsuario = read('classpath:petstore/usuarios/schemas/usuario-schema.json')
    * def esquemaRespuesta = read('classpath:petstore/usuarios/schemas/respuesta-api-schema.json')
    * def nuevoNombre = 'Laura Sofia'
    * def nuevoCorreo = 'laura.sofia.' + datos.id + '@example.com'
    * configure afterScenario = function(){ karate.call('classpath:petstore/usuarios/helpers/eliminar-usuario.feature', { nombreUsuario: nombreUsuario }) }

  @crear
  Scenario: Crear un usuario
    Given path 'user'
    And request usuario
    When method post
    Then status 200
    And match response == esquemaRespuesta
    And match response.code == 200
    And match response.message == datos.id + ''
    * print 'Usuario creado -> id:', response.message, '| username:', nombreUsuario

  @buscar
  Scenario: Buscar el usuario creado
    * call read('classpath:petstore/usuarios/helpers/crear-usuario.feature') { cuerpo: '#(usuario)' }
    Given path 'user', nombreUsuario
    And retry until responseStatus == 200
    When method get
    Then status 200
    And match response == esquemaUsuario
    And match response == usuario
    * print 'Usuario encontrado:', response

  @actualizar
  Scenario: Actualizar el nombre y el correo del usuario
    * call read('classpath:petstore/usuarios/helpers/crear-usuario.feature') { cuerpo: '#(usuario)' }
    * copy usuarioActualizado = usuario
    * set usuarioActualizado.firstName = nuevoNombre
    * set usuarioActualizado.email = nuevoCorreo
    Given path 'user', nombreUsuario
    And request usuarioActualizado
    When method put
    Then status 200
    And match response == esquemaRespuesta
    And match response.code == 200
    And match response.message == datos.id + ''

  @buscar_actualizado
  Scenario: Buscar el usuario actualizado
    * call read('classpath:petstore/usuarios/helpers/crear-usuario.feature') { cuerpo: '#(usuario)' }
    * copy usuarioActualizado = usuario
    * set usuarioActualizado.firstName = nuevoNombre
    * set usuarioActualizado.email = nuevoCorreo
    * call read('classpath:petstore/usuarios/helpers/actualizar-usuario.feature') { nombreUsuario: '#(nombreUsuario)', cuerpo: '#(usuarioActualizado)' }
    Given path 'user', nombreUsuario
    And retry until responseStatus == 200 && response.email == nuevoCorreo
    When method get
    Then status 200
    And match response == esquemaUsuario
    And match response.firstName == nuevoNombre
    And match response.email == nuevoCorreo
    And match response == usuarioActualizado
    * print 'Antes:', usuario.firstName, '/', usuario.email, '| Después:', response.firstName, '/', response.email

  @eliminar
  Scenario: Eliminar el usuario
    * call read('classpath:petstore/usuarios/helpers/crear-usuario.feature') { cuerpo: '#(usuario)' }
    Given path 'user', nombreUsuario
    And retry until responseStatus == 200
    When method delete
    Then status 200
    And match response == { code: 200, type: 'unknown', message: '#(nombreUsuario)' }

    # Verificación: el usuario eliminado ya no se puede consultar
    Given path 'user', nombreUsuario
    And retry until responseStatus == 404
    When method get
    Then status 404
    And match response == { code: 1, type: 'error', message: 'User not found' }

  @negativo
  Scenario: Consultar un usuario inexistente retorna 404
    * def usuarioInexistente = 'no_existe_' + nombreUsuario
    Given path 'user', usuarioInexistente
    When method get
    Then status 404
    And match response == { code: 1, type: 'error', message: 'User not found' }
