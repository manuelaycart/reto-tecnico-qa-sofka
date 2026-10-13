package com.retoqa.opencart.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class OrderSuccessPage {

    public static final String URL_FRAGMENT = "route=checkout/success";

    public static final Target TITLE = Target.the("título de la confirmación del pedido")
            .located(By.cssSelector("#content h1"));

    private OrderSuccessPage() {
    }
}
