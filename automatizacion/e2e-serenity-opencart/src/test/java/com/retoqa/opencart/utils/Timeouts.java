package com.retoqa.opencart.utils;

/**
 * Tiempos de espera explícitos. OpenCart arma el checkout con varias llamadas
 * AJAX y el ambiente de demo es compartido, por eso se usa un margen holgado.
 */
public final class Timeouts {

    public static final int MAX_WAIT_SECONDS = 30;

    private Timeouts() {
    }
}
