package com.retoqa.opencart.questions;

import com.retoqa.opencart.userinterfaces.CartPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.util.List;

/**
 * Nombres de los productos listados en la tabla del carrito.
 */
public class ProductsInCart implements Question<List<String>> {

    @Override
    public List<String> answeredBy(Actor actor) {
        return CartPage.PRODUCT_NAMES.resolveAllFor(actor).stream()
                .map(element -> element.getText().trim())
                .toList();
    }

    public static ProductsInCart names() {
        return new ProductsInCart();
    }
}
