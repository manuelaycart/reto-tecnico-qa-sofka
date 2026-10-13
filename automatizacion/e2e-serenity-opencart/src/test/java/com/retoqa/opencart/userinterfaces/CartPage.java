package com.retoqa.opencart.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class CartPage {

    public static final String URL_FRAGMENT = "route=checkout/cart";

    // No se usa el h1 para identificar la página: en este ambiente muestra
    // "Use Gift Certificate" en lugar de "Shopping Cart" (defecto reportado en conclusiones.txt).
    public static final Target PRODUCTS_TABLE = Target.the("tabla de productos del carrito")
            .located(By.cssSelector("#content form table"));

    public static final Target PRODUCT_NAMES = Target.the("nombres de los productos del carrito")
            .located(By.cssSelector("#content form table tbody td.text-left > a"));

    public static final Target STOCK_WARNING = Target.the("alerta de productos sin stock")
            .locatedBy("//div[contains(@class,'alert-danger')][contains(.,'not available')]");

    public static final Target CHECKOUT_BUTTON = Target.the("botón Checkout del carrito")
            .locatedBy("//div[@id='content']//a[contains(@class,'btn') and normalize-space()='Checkout']");

    private CartPage() {
    }
}
