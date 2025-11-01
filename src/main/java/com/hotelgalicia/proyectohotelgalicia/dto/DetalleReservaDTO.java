package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleReservaDTO {
    @NotNull(message = "La habitación no puede estar vacía.")
    private Long habitacion;

    @Min(value = 1, message = "Dee haber al menos una habitación reservada.")
    private Integer cantidad;
}
