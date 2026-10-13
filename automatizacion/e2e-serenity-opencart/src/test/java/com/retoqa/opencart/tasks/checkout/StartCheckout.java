package com.retoqa.opencart.tasks.checkout;

import com.retoqa.opencart.userinterfaces.CartPage;
import com.retoqa.opencart.userinterfaces.CheckoutPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class StartCheckout implements Task {

    @Override
    @Step("{0} inicia el checkout desde el carrito")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(CartPage.CHECKOUT_BUTTON),
                WaitUntil.the(CheckoutPage.GUEST_CHECKOUT_OPTION, isClickable())
                        .forNoMoreThan(MAX_WAIT_SECONDS).seconds()
        );
    }

    public static StartCheckout fromTheCart() {
        return instrumented(StartCheckout.class);
    }
}
