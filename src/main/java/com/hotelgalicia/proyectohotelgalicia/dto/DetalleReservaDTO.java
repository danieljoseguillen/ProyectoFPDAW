package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleReservaDTO {
    @NotNull(message = "La habitación no puede estar vacía.")
    private Long habitacion;

    @Min(value = 1, message = "Dee haber al menos una habitación reservada.")
    private Integer cantidad;
}
