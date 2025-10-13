package com.hotelgalicia.proyectohotelgalicia.excepciones;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
