package com.hotelgalicia.proyectohotelgalicia.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleReservaId implements Serializable {
    private Long habitacion;
    private Long reserva;

}
