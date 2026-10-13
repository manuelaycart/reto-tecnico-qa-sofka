package com.retoqa.opencart.tasks;

import com.retoqa.opencart.interactions.WaitForUrl;
import com.retoqa.opencart.userinterfaces.CartPage;
import com.retoqa.opencart.userinterfaces.HeaderComponent;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class ViewTheCart implements Task {

    @Override
    @Step("{0} abre su carrito de compras")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(HeaderComponent.SHOPPING_CART_LINK),
                WaitForUrl.containing(CartPage.URL_FRAGMENT),
                WaitUntil.the(CartPage.PRODUCTS_TABLE, isVisible()).forNoMoreThan(MAX_WAIT_SECONDS).seconds()
        );
    }

    public static ViewTheCart contents() {
        return instrumented(ViewTheCart.class);
    }
}
