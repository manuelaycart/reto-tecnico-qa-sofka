package com.retoqa.opencart.tasks.checkout;

import com.retoqa.opencart.models.Customer;
import com.retoqa.opencart.userinterfaces.CheckoutPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isPresent;

public class FillBillingDetails implements Task {

    private final Customer customer;

    public FillBillingDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    @Step("{0} ingresa los datos de facturación de #customer (paso 2)")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(customer.firstName()).into(CheckoutPage.FIRST_NAME),
                Enter.theValue(customer.lastName()).into(CheckoutPage.LAST_NAME),
                Enter.theValue(customer.email()).into(CheckoutPage.EMAIL),
                Enter.theValue(customer.telephone()).into(CheckoutPage.TELEPHONE),
                Enter.theValue(customer.address()).into(CheckoutPage.ADDRESS),
                Enter.theValue(customer.city()).into(CheckoutPage.CITY),
                Enter.theValue(customer.postCode()).into(CheckoutPage.POST_CODE),
                SelectFromOptions.byVisibleText(customer.country()).from(CheckoutPage.COUNTRY),
                // Las regiones se recargan por AJAX al cambiar el país
                WaitUntil.the(CheckoutPage.REGION_OPTION.of(customer.region()), isPresent())
                        .forNoMoreThan(MAX_WAIT_SECONDS).seconds(),
                SelectFromOptions.byVisibleText(customer.region()).from(CheckoutPage.REGION),
                Click.on(CheckoutPage.CONTINUE_BILLING_DETAILS)
        );
    }

    public static FillBillingDetails with(Customer customer) {
        return instrumented(FillBillingDetails.class, customer);
    }
}
