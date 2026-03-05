package com.hotelgalicia.proyectohotelgalicia.excepciones;

public class RoomFullException extends RuntimeException {
    public RoomFullException(String name, Integer num) {
        super(name + " no tiene suficientes habitaciones disponibles. No se pueden reservar " + num + " habitaciones.");
    }
}
