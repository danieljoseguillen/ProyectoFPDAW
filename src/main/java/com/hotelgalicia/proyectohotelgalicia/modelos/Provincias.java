package com.hotelgalicia.proyectohotelgalicia.modelos;

public enum Provincias {
    A_CORUNA("A Coruña"),
    LUGO("Lugo"),
    OURENSE("Ourense"),
    PONTEVEDRA("Pontevedra");

    private final String nombre;

    Provincias(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
