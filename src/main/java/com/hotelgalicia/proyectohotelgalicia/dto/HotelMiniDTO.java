package com.hotelgalicia.proyectohotelgalicia.dto;

import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HotelMiniDTO {
    private Long id;
    private String nombre;
    private Municipios municipio;
    private String direccion;
    private String imagen;
    private double puntaje;
    private Integer valoraciones;
    private HabitacionMiniDTO habitacion;
}
