package com.retoqa.opencart.questions;

import com.retoqa.opencart.userinterfaces.CartPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Indica si el carrito muestra la alerta "Products marked with *** are not available...".
 * Si aparece, OpenCart no permite continuar al checkout, así que se valida antes de pagar
 * para que la falla quede explicada en el reporte.
 */
public class CartStockWarning implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        return !CartPage.STOCK_WARNING.resolveAllFor(actor).isEmpty();
    }

    public static CartStockWarning isDisplayed() {
        return new CartStockWarning();
    }
}
