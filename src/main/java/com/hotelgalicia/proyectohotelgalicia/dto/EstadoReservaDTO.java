package com.hotelgalicia.proyectohotelgalicia.dto;

import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoReservaDTO {

private Long id;

private EstadoReserva estado;
}
