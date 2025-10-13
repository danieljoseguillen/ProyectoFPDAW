package com.hotelgalicia.proyectohotelgalicia.dto;

import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelDTO {

    @NotNull(message = "El hotel debe tener un nombre.")
    @Size(min = 5, message = "Debe tener un minimo de 5 caracteres")
    private String nombre;

    @NotNull(message = "El hotel debe tener un nombre.")
    @Size(min = 10, message = "Debe tener un minimo de 10 caracteres")
    @Column(length = 5000)
    private String descripcion;

    private Municipios municipio;

    @NotEmpty(message = "El hotel debe tener una dirección registrada.")
    @NotNull(message = "El hotel debe tener una dirección registrada.")
    private String direccion;

    @Size(min = 9, message = "Debe tener un minimo de 9 caracteres")
    private String telefono;
}
