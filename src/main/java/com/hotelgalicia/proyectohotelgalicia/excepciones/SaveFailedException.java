package com.hotelgalicia.proyectohotelgalicia.excepciones;

public class SaveFailedException extends RuntimeException{
    public SaveFailedException(String msg) {
        super(msg);
    }
}
