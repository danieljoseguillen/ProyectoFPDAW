package com.hotelgalicia.proyectohotelgalicia.modelos;

public enum FiltroBusqueda {
    PRECIO_DESCENDENTE("Precio (Mas alto primero)"),
    PRECIO_ASCENDENTE("Precio (Mas bajo primero)"),
    VALORACION_DESCENDENTE("Valoracion (Mas alta primero)"),
    VALORACION_ASCENDENTE("Valoracion (Mas baja primero)"),
    VALORACION_PRECIO_ASCENDENTE("Mejor valoración y precio mas bajo"),
    MAS_VALORADOS("Mas valorados");

    private final String nombre;

    FiltroBusqueda(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
