package com.retoqa.opencart.tasks.checkout;

import com.retoqa.opencart.userinterfaces.CheckoutPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class ConfirmDeliveryMethod implements Task {

    @Override
    @Step("{0} confirma el método de envío (paso 4)")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(CheckoutPage.CONTINUE_DELIVERY_METHOD, isClickable())
                        .forNoMoreThan(MAX_WAIT_SECONDS).seconds(),
                Click.on(CheckoutPage.CONTINUE_DELIVERY_METHOD)
        );
    }

    public static ConfirmDeliveryMethod selected() {
        return instrumented(ConfirmDeliveryMethod.class);
    }
}
