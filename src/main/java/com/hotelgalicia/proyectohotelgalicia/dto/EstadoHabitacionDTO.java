package com.hotelgalicia.proyectohotelgalicia.dto;

import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoHabitacionDTO {

    Long id;

    private EstadoHabitacion estado;
}
