package com.hotelgalicia.proyectohotelgalicia.excepciones;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String msg) {
        super(msg);
    }
}
