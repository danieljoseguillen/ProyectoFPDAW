package com.hotelgalicia.proyectohotelgalicia.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DetalleReservaId.class)
public class DetalleReserva {
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Habitacion habitacion;
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Reserva reserva;

    @Min(value = 1, message = "Dee haber al menos una habitación reservada.")
    private Integer cantidad;

    private String nombre;

    private Double precio;
}
