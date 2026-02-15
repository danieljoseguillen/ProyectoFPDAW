package com.hotelgalicia.proyectohotelgalicia.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La habitación debe tener un nombre.")
    @Size(min = 10, message = "Debe tener un minimo de 10 caracteres")
    private String nombre;

    @NotNull(message = "La habitacion debe tener un nombre.")
    @Size(min = 5, message = "Debe tener un minimo de 5 caracteres")
    @Column(length = 3000)
    private String descripcion;

    @Min(value = 1, message = "Debe haber por lo menos una habitación de ese tipo")
    private Integer cantidad;

    @Min(value = 1, message = "La habitacion debe poder alojar por lo menos a una persona.")
    private Integer capacidad;

    @Min(value = 0, message = "Precio fuera de rango.")
    private Double precio;

    private String imagen;

    @NotNull(message = "La habitación debe tener un estado.")
    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estado;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Hotel hotel;
}
