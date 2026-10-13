package com.retoqa.opencart.tasks;

import com.retoqa.opencart.userinterfaces.HeaderComponent;
import com.retoqa.opencart.userinterfaces.StoreHomePage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class OpenTheStore implements Task {

    @Override
    @Step("{0} abre la tienda OpenCart")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.browserOn().the(StoreHomePage.class),
                WaitUntil.the(HeaderComponent.SEARCH_INPUT, isVisible()).forNoMoreThan(MAX_WAIT_SECONDS).seconds()
        );
    }

    public static OpenTheStore onTheHomePage() {
        return instrumented(OpenTheStore.class);
    }
}
