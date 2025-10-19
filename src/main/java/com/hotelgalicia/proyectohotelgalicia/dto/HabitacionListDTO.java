package com.hotelgalicia.proyectohotelgalicia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionListDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private Integer capacidad;

    private Double precio;

    private String imagen;

    private Integer disponibles;

}
