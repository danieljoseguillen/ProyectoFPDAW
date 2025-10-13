package com.hotelgalicia.proyectohotelgalicia.excepciones;

public class RoomFullException extends RuntimeException {
    public RoomFullException(String name, Integer num, Integer num2) {
        super("La habitación " + name + "solo posee " + num + " plazas disponibles. No se pueden reservar " + num2 + " plazas.");
    }
}
