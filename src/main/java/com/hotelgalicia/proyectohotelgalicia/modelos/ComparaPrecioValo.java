package com.hotelgalicia.proyectohotelgalicia.modelos;

import java.util.Comparator;

import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;

public class ComparaPrecioValo implements Comparator<HotelMiniDTO>{
    @Override
    public int compare(HotelMiniDTO h1, HotelMiniDTO h2) {
        Double precio1 = h1.getHabitacion().getPrecio();
        Double precio2 = h2.getHabitacion().getPrecio();
        int resultadoPrecio = precio1.compareTo(precio2);
        if (resultadoPrecio != 0) {
            return resultadoPrecio;
        } else {
            // Si los precios son iguales, compara por puntaje
            Double valoracion1 = h1.getPuntaje();
            Double valoracion2 = h2.getPuntaje();
            return valoracion2.compareTo(valoracion1);
        }
    }
}
