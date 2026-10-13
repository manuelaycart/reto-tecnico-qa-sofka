package com.retoqa.opencart.stepdefinitions;

import com.retoqa.opencart.models.Customer;
import com.retoqa.opencart.questions.CartStockWarning;
import com.retoqa.opencart.questions.OrderConfirmation;
import com.retoqa.opencart.questions.ProductsInCart;
import com.retoqa.opencart.tasks.AddToCart;
import com.retoqa.opencart.tasks.CheckoutAsGuest;
import com.retoqa.opencart.tasks.OpenTheStore;
import com.retoqa.opencart.tasks.ViewTheCart;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.Actor;

import java.util.List;
import java.util.Map;

import static com.retoqa.opencart.utils.MemoryKeys.PRODUCTS_ADDED;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CompraInvitadoStepDefinitions {

    @Dado("que {actor} ingresa a la tienda OpenCart")
    public void ingresaALaTiendaOpenCart(Actor actor) {
        actor.wasAbleTo(OpenTheStore.onTheHomePage());
    }

    @Cuando("agrega al carrito los productos:")
    public void agregaAlCarritoLosProductos(List<String> productos) {
        theActorInTheSpotlight().attemptsTo(AddToCart.theProducts(productos));
    }

    @Y("revisa su carrito de compras")
    public void revisaSuCarritoDeCompras() {
        theActorInTheSpotlight().attemptsTo(ViewTheCart.contents());
    }

    @Entonces("debería ver en el carrito los productos agregados")
    public void deberiaVerEnElCarritoLosProductosAgregados() {
        List<String> productosAgregados = theActorInTheSpotlight().recall(PRODUCTS_ADDED);

        theActorInTheSpotlight().should(
                seeThat("los productos del carrito", ProductsInCart.names(),
                        containsInAnyOrder(productosAgregados.toArray(new String[0]))),
                seeThat("la alerta de productos sin stock", CartStockWarning.isDisplayed(), is(false))
        );
    }

    @Cuando("completa el checkout como invitado con los datos:")
    public void completaElCheckoutComoInvitado(Map<String, String> datos) {
        theActorInTheSpotlight().attemptsTo(CheckoutAsGuest.withTheData(Customer.fromTable(datos)));
    }

    @Entonces("debería ver el mensaje de confirmación {string}")
    public void deberiaVerElMensajeDeConfirmacion(String mensajeEsperado) {
        theActorInTheSpotlight().should(
                seeThat("el título de la página de confirmación", OrderConfirmation.title(),
                        equalTo(mensajeEsperado))
        );
    }
}
