package com.retoqa.opencart.tasks;

import com.retoqa.opencart.userinterfaces.HeaderComponent;
import com.retoqa.opencart.userinterfaces.SearchResultsPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.Keys;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AddProductToCart implements Task {

    private final String product;

    public AddProductToCart(String product) {
        this.product = product;
    }

    @Override
    @Step("{0} busca el producto '#product' y lo agrega al carrito")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(product).into(HeaderComponent.SEARCH_INPUT).thenHit(Keys.ENTER),
                WaitUntil.the(SearchResultsPage.ADD_TO_CART_BUTTON.of(product), isClickable())
                        .forNoMoreThan(MAX_WAIT_SECONDS).seconds(),
                Click.on(SearchResultsPage.ADD_TO_CART_BUTTON.of(product)),
                WaitUntil.the(HeaderComponent.SUCCESS_ALERT_FOR_PRODUCT.of(product), isVisible())
                        .forNoMoreThan(MAX_WAIT_SECONDS).seconds()
        );
    }

    public static AddProductToCart named(String product) {
        return instrumented(AddProductToCart.class, product);
    }
}
