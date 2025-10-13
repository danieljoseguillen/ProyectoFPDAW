package com.hotelgalicia.proyectohotelgalicia.dto;

import java.time.LocalDate;

import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelSearchDTO {

    @NotNull
    private String nombre;

    private Municipios municipio;
    
    @NotNull
    private String direccion;

    @Min(value = 1, message = "Debe haber al menos una persona en la busqueda")
    private int personas;

    @Min(value = 1, message = "Debe haber al menos una habitación en la busqueda.")
    private int habitaciones;
    @NotNull(message = "Debe ingresar una fecha de inicio.")
    @FutureOrPresent(message = "La fecha de inicio de la reserva debe ser la fecha actual o posterior.")
    private LocalDate fechaInicio;

    @NotNull(message = "Debe ingresar una fecha de fin.")
    @Future(message = "La fecha de salida debe ser una fecha posterior.")
    private LocalDate fechaFin;

    @Min(value = 5)
    private int presupuestoMin;
    @Min(value = 5)
    private int presupuestoMax;
}
