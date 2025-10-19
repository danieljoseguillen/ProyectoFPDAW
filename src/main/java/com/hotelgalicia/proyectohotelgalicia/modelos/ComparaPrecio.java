package com.hotelgalicia.proyectohotelgalicia.modelos;

import java.util.Comparator;

import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;

public class ComparaPrecio implements Comparator<HotelMiniDTO>{
    @Override
    public int compare(HotelMiniDTO h1, HotelMiniDTO h2) {
        Double precio1 = h1.getHabitacion().getPrecio();
        Double precio2 = h2.getHabitacion().getPrecio();
        return precio1.compareTo(precio2);
    }
}
