package com.retoqa.opencart.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Resultados de búsqueda. El botón se ubica por el nombre exacto del producto
 * porque una búsqueda puede traer varios resultados (ej. "MacBook" trae también Air y Pro).
 */
public final class SearchResultsPage {

    public static final Target ADD_TO_CART_BUTTON = Target.the("botón Add to Cart del producto {0}")
            .locatedBy("//div[contains(@class,'product-thumb')][.//h4/a[normalize-space()='{0}']]"
                    + "//button[contains(@onclick,'cart.add')]");

    private SearchResultsPage() {
    }
}
