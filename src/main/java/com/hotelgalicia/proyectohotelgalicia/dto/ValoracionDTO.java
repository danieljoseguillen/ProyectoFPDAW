package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValoracionDTO {
    @NotNull(message = "El hotel es obligatorio")
    private Long hotel;
    @Min(value = 0, message = "La valoración minima es 0.")
    @Max(value = 10, message = "La valoración maxima es de 10.")
    private Integer puntaje;
    private String comentario;
}
