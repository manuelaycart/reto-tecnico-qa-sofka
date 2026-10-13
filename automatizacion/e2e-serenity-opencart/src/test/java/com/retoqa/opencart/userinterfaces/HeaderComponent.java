package com.retoqa.opencart.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Elementos del encabezado, presentes en todas las páginas de la tienda.
 */
public final class HeaderComponent {

    public static final Target SEARCH_INPUT = Target.the("campo de búsqueda")
            .located(By.cssSelector("#search input[name='search']"));

    public static final Target SHOPPING_CART_LINK = Target.the("enlace Shopping Cart del encabezado")
            .located(By.cssSelector("#top-links a[title='Shopping Cart']"));

    public static final Target SUCCESS_ALERT_FOR_PRODUCT = Target.the("mensaje de producto {0} agregado al carrito")
            .locatedBy("//div[contains(@class,'alert-success')][.//a[normalize-space()='{0}']]");

    private HeaderComponent() {
    }
}
