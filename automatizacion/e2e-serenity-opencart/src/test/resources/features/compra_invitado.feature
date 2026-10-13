# language: es
@compra @guest_checkout
Característica: Compra de productos como invitado en OpenCart
  Como cliente de la tienda en línea OpenCart
  Quiero comprar productos sin registrarme
  Para completar mi pedido de forma rápida

  @e2e
  Escenario: Compra exitosa de dos productos mediante Guest Checkout
    Dado que Laura ingresa a la tienda OpenCart
    Cuando agrega al carrito los productos:
      | iPod Classic |
      | HTC Touch HD |
    Y revisa su carrito de compras
    Entonces debería ver en el carrito los productos agregados
    Cuando completa el checkout como invitado con los datos:
      | nombre        | Laura                   |
      | apellido      | Gomez                   |
      | correo        | laura.gomez@example.com |
      | teléfono      | 0991234567              |
      | dirección     | Av. 9 de Octubre 1520   |
      | ciudad        | Guayaquil               |
      | código postal | 090313                  |
      | país          | Ecuador                 |
      | región        | Guayas                  |
    Entonces debería ver el mensaje de confirmación "Your order has been placed!"
