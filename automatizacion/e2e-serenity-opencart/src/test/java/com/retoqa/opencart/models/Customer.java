package com.retoqa.opencart.models;

import java.util.Map;

/**
 * Datos del cliente invitado que se ingresan en el checkout.
 * Se construye a partir de la tabla clave/valor del feature.
 */
public record Customer(String firstName,
                       String lastName,
                       String email,
                       String telephone,
                       String address,
                       String city,
                       String postCode,
                       String country,
                       String region) {

    public static Customer fromTable(Map<String, String> data) {
        return new Customer(
                required(data, "nombre"),
                required(data, "apellido"),
                required(data, "correo"),
                required(data, "teléfono"),
                required(data, "dirección"),
                required(data, "ciudad"),
                required(data, "código postal"),
                required(data, "país"),
                required(data, "región"));
    }

    private static String required(Map<String, String> data, String field) {
        String value = data.get(field);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El feature no define el dato obligatorio '" + field + "'");
        }
        return value.trim();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }
}
