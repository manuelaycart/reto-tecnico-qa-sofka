package com.retoqa.opencart.tasks.checkout;

import com.retoqa.opencart.interactions.WaitForUrl;
import com.retoqa.opencart.userinterfaces.CheckoutPage;
import com.retoqa.opencart.userinterfaces.OrderSuccessPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class ConfirmTheOrder implements Task {

    @Override
    @Step("{0} confirma el pedido (paso 6)")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(CheckoutPage.CONFIRM_ORDER, isClickable()).forNoMoreThan(MAX_WAIT_SECONDS).seconds(),
                Click.on(CheckoutPage.CONFIRM_ORDER),
                WaitForUrl.containing(OrderSuccessPage.URL_FRAGMENT)
        );
    }

    public static ConfirmTheOrder now() {
        return instrumented(ConfirmTheOrder.class);
    }
}
