function fn() {
  // Datos unicos en cada ejecucion para no chocar con otros usuarios de la API publica.
  var sufijo = Date.now() + '' + Math.floor(Math.random() * 1000);
  return {
    id: Math.floor(Math.random() * 900000000) + 100000000,
    username: 'qa_reto_' + sufijo,
    email: 'laura.gomez.' + sufijo + '@example.com'
  };
}
