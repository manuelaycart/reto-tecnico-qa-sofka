package com.retoqa.opencart.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.retoqa.opencart.utils.Timeouts.MAX_WAIT_SECONDS;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Espera a que el navegador llegue a una URL. Se usa tras confirmar el pedido:
 * el checkout y la página de éxito tienen ambos un h1 en #content, así que esperar
 * por el elemento no basta para saber que la navegación terminó.
 */
public class WaitForUrl implements Interaction {

    private final String fragment;

    public WaitForUrl(String fragment) {
        this.fragment = fragment;
    }

    @Override
    @Step("{0} espera a que la URL contenga '#fragment'")
    public <T extends Actor> void performAs(T actor) {
        new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(MAX_WAIT_SECONDS))
                .until(ExpectedConditions.urlContains(fragment));
    }

    public static WaitForUrl containing(String fragment) {
        return instrumented(WaitForUrl.class, fragment);
    }
}
