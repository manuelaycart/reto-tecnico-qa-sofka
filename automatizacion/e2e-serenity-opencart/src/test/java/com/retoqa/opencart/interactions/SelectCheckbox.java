package com.retoqa.opencart.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Marca una casilla solo si no está marcada. Un clic directo la desmarcaría si
 * la página la recordara seleccionada de un intento anterior.
 */
public class SelectCheckbox implements Interaction {

    private final Target checkbox;

    public SelectCheckbox(Target checkbox) {
        this.checkbox = checkbox;
    }

    @Override
    @Step("{0} marca la #checkbox")
    public <T extends Actor> void performAs(T actor) {
        WebElementFacade element = checkbox.resolveFor(actor);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public static SelectCheckbox on(Target checkbox) {
        return instrumented(SelectCheckbox.class, checkbox);
    }
}
