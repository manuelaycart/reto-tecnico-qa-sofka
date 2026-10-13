package com.retoqa.opencart.tasks.checkout;

import com.retoqa.opencart.interactions.SelectCheckbox;
import com.retoqa.opencart.userinterfaces.CheckoutPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class ConfirmPaymentMethod implements Task {

    @Override
    @Step("{0} acepta los términos y confirma el método de pago (paso 5)")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(CheckoutPage.CONTINUE_PAYMENT_METHOD, isClickable())
                        .forNoMoreThan(MAX_WAIT_SECONDS).seconds(),
                SelectCheckbox.on(CheckoutPage.TERMS_AND_CONDITIONS),
                Click.on(CheckoutPage.CONTINUE_PAYMENT_METHOD)
        );
    }

    public static ConfirmPaymentMethod acceptingTheTerms() {
        return instrumented(ConfirmPaymentMethod.class);
    }
}
