package com.retoqa.opencart.stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class Hooks {

    @Before
    public void prepararElEscenario() {
        // Cada actor recibe un navegador administrado por Serenity
        OnStage.setTheStage(new OnlineCast());
    }
}
