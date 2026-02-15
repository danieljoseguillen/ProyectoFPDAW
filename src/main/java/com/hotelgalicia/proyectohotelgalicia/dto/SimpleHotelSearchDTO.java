package com.hotelgalicia.proyectohotelgalicia.dto;

import com.hotelgalicia.proyectohotelgalicia.modelos.FiltroBusquedaAdmin;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleHotelSearchDTO {

    @NotNull
    private String nombre;

    private Municipios municipio;

    @NotNull
    private String direccion;

    private FiltroBusquedaAdmin filtro;
}
