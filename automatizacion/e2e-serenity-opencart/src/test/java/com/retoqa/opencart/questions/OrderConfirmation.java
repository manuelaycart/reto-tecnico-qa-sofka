package com.retoqa.opencart.questions;

import com.retoqa.opencart.userinterfaces.OrderSuccessPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Título que muestra OpenCart al terminar la compra.
 */
public class OrderConfirmation implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        return OrderSuccessPage.TITLE.resolveFor(actor).getText().trim();
    }

    public static OrderConfirmation title() {
        return new OrderConfirmation();
    }
}
