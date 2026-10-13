package com.retoqa.opencart.stepdefinitions;

import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

public class ParameterDefinitions {

    /**
     * Convierte el nombre escrito en el feature ("Laura") en un actor de Screenplay
     * y lo deja como protagonista del escenario.
     */
    @ParameterType(".*")
    public Actor actor(String nombreDelActor) {
        return OnStage.theActorCalled(nombreDelActor);
    }
}
