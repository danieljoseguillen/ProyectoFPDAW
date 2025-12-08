package com.hotelgalicia.proyectohotelgalicia.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Debe ingresar una fecha de inicio.")
    @FutureOrPresent(message = "La fecha de inicio de la reserva debe ser la fecha actual o posterior.")
    private LocalDate fechaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Debe ingresar una fecha de fin.")
    @Future(message = "La fecha de salida debe ser una fecha posterior.")
    private LocalDate fechaFin;

    @Min(value = 1, message = "Debe haber al menos una persona en la reserva")
    private Integer personas;

    @Size(min = 1, message = "Debe haber al menos una habitación en la reserva.")
    private List<DetalleReservaDTO> habitaciones = new ArrayList<>();
}
