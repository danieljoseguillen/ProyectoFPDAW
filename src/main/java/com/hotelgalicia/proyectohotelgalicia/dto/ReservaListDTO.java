package com.hotelgalicia.proyectohotelgalicia.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaListDTO {

    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaFin;

    private Integer personas;

    private EstadoReserva estado;

    private String nombre;

    private Long nombreId;

    private Integer habitacionestotal;

    private Double costototal;
}
