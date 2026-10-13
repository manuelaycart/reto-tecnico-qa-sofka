package com.retoqa.opencart.tasks.checkout;

import com.retoqa.opencart.userinterfaces.CheckoutPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class ChooseGuestCheckout implements Task {

    @Override
    @Step("{0} elige la opción Guest Checkout (paso 1)")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(CheckoutPage.GUEST_CHECKOUT_OPTION),
                Click.on(CheckoutPage.CONTINUE_CHECKOUT_OPTIONS),
                WaitUntil.the(CheckoutPage.FIRST_NAME, isVisible()).forNoMoreThan(MAX_WAIT_SECONDS).seconds()
        );
    }

    public static ChooseGuestCheckout option() {
        return instrumented(ChooseGuestCheckout.class);
    }
}
