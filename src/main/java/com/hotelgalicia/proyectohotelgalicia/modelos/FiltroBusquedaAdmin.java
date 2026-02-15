package com.hotelgalicia.proyectohotelgalicia.modelos;

public enum FiltroBusquedaAdmin {
    NOMBRE_ASCENDENTE("Nombre (A-Z)"),
    NOMBRE_DESCENDENTE("Nombre (Z-A)"),
    VALORACION_DESCENDENTE("Valoracion (Mas alta primero)"),
    VALORACION_ASCENDENTE("Valoracion (Mas baja primero)"),
    MAS_VALORADOS("Mas valorados");

    private final String nombre;

    FiltroBusquedaAdmin(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
