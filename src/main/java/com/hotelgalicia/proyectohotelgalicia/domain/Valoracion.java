package com.hotelgalicia.proyectohotelgalicia.domain;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ValoracionID.class)
public class Valoracion {

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hotel hotel;
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cliente cliente;

    @NotNull(message = "Debe tener una fecha de valoración.")
    private LocalDate fecha;

    @Min(value = 0, message = "La valoración minima es 0.")
    @Max(value = 10, message = "La valoración maxima es de 10.")
    private Integer puntaje;
    private String comentario;
}
