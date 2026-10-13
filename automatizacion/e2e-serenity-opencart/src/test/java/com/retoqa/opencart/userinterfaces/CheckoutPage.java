package com.retoqa.opencart.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Checkout de OpenCart: un acordeón de pasos que se cargan por AJAX.
 */
public final class CheckoutPage {

    // Paso 1 - Opciones de checkout
    public static final Target GUEST_CHECKOUT_OPTION = Target.the("opción Guest Checkout")
            .located(By.cssSelector("input[name='account'][value='guest']"));
    public static final Target CONTINUE_CHECKOUT_OPTIONS = Target.the("botón Continue de opciones de checkout")
            .located(By.id("button-account"));

    // Paso 2 - Datos de facturación del invitado
    public static final Target FIRST_NAME = Target.the("campo First Name")
            .located(By.id("input-payment-firstname"));
    public static final Target LAST_NAME = Target.the("campo Last Name")
            .located(By.id("input-payment-lastname"));
    public static final Target EMAIL = Target.the("campo E-Mail")
            .located(By.id("input-payment-email"));
    public static final Target TELEPHONE = Target.the("campo Telephone")
            .located(By.id("input-payment-telephone"));
    public static final Target ADDRESS = Target.the("campo Address 1")
            .located(By.id("input-payment-address-1"));
    public static final Target CITY = Target.the("campo City")
            .located(By.id("input-payment-city"));
    public static final Target POST_CODE = Target.the("campo Post Code")
            .located(By.id("input-payment-postcode"));
    public static final Target COUNTRY = Target.the("lista Country")
            .located(By.id("input-payment-country"));
    public static final Target REGION = Target.the("lista Region / State")
            .located(By.id("input-payment-zone"));
    public static final Target REGION_OPTION = Target.the("opción {0} de Region / State")
            .locatedBy("//select[@id='input-payment-zone']/option[normalize-space()='{0}']");
    public static final Target CONTINUE_BILLING_DETAILS = Target.the("botón Continue de datos de facturación")
            .located(By.id("button-guest"));

    // Paso 4 - Método de envío
    public static final Target CONTINUE_DELIVERY_METHOD = Target.the("botón Continue de método de envío")
            .located(By.id("button-shipping-method"));

    // Paso 5 - Método de pago
    public static final Target TERMS_AND_CONDITIONS = Target.the("casilla de Terms & Conditions")
            .located(By.cssSelector("input[name='agree']"));
    public static final Target CONTINUE_PAYMENT_METHOD = Target.the("botón Continue de método de pago")
            .located(By.id("button-payment-method"));

    // Paso 6 - Confirmación
    public static final Target CONFIRM_ORDER = Target.the("botón Confirm Order")
            .located(By.id("button-confirm"));

    private CheckoutPage() {
    }
}
