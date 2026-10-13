package com.retoqa.opencart.tasks;

import com.retoqa.opencart.models.Customer;
import com.retoqa.opencart.tasks.checkout.ChooseGuestCheckout;
import com.retoqa.opencart.tasks.checkout.ConfirmDeliveryMethod;
import com.retoqa.opencart.tasks.checkout.ConfirmPaymentMethod;
import com.retoqa.opencart.tasks.checkout.ConfirmTheOrder;
import com.retoqa.opencart.tasks.checkout.FillBillingDetails;
import com.retoqa.opencart.tasks.checkout.StartCheckout;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Recorre el checkout completo como invitado. Cada paso del acordeón de OpenCart
 * es una tarea propia para que el reporte muestre exactamente dónde falla.
 */
public class CheckoutAsGuest implements Task {

    private final Customer customer;

    public CheckoutAsGuest(Customer customer) {
        this.customer = customer;
    }

    @Override
    @Step("{0} completa el checkout como invitado")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                StartCheckout.fromTheCart(),
                ChooseGuestCheckout.option(),
                FillBillingDetails.with(customer),
                ConfirmDeliveryMethod.selected(),
                ConfirmPaymentMethod.acceptingTheTerms(),
                ConfirmTheOrder.now()
        );
    }

    public static CheckoutAsGuest withTheData(Customer customer) {
        return instrumented(CheckoutAsGuest.class, customer);
    }
}
