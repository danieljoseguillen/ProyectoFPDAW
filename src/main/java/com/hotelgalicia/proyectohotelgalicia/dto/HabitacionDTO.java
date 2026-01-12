package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitacionDTO {
    @NotNull(message = "La habitación debe tener un nombre.")
    @Size(min = 10, message = "Debe tener un minimo de 10 caracteres")
    private String nombre;

    @NotNull(message = "La habitacion debe tener un nombre.")
    @Size(min = 5, message = "Debe tener un minimo de 5 caracteres")
    @Column(length = 3000)
    private String descripcion;

    @NotNull(message = "Debe haber por lo menos una habitación de ese tipo.")
    @Min(value = 1, message = "Debe haber por lo menos una habitación de ese tipo")
    private Integer cantidad;

    @NotNull(message = "La habitacion debe poder alojar por lo menos a una persona.")
    @Min(value = 1, message = "La habitacion debe poder alojar por lo menos a una persona.")
    private Integer capacidad;

    @NotNull(message = "El precio no puede estar vacío.")
    @Min(value = 0, message = "Precio fuera de rango.")
    private Double precio;
}
