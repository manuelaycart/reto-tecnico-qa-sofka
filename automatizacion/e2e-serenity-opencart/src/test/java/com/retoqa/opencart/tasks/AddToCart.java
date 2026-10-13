package com.retoqa.opencart.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import java.util.ArrayList;
import java.util.List;

import static com.retoqa.opencart.utils.MemoryKeys.PRODUCTS_ADDED;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AddToCart implements Task {

    private final List<String> products;

    public AddToCart(List<String> products) {
        this.products = new ArrayList<>(products);
    }

    @Override
    @Step("{0} agrega al carrito los productos #products")
    public <T extends Actor> void performAs(T actor) {
        products.forEach(product -> actor.attemptsTo(AddProductToCart.named(product)));
        actor.remember(PRODUCTS_ADDED, List.copyOf(products));
    }

    public static AddToCart theProducts(List<String> products) {
        return instrumented(AddToCart.class, products);
    }
}
