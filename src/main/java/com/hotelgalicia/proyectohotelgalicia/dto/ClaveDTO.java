package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClaveDTO {

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @NotBlank(message = "La contraseña es obligatoria.")
    private String claveActual;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @NotBlank(message = "La contraseña es obligatoria.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$", message = "La contraseña debe contener al menos una letra mayúscula y un número.")
    private String claveRepetir;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @NotBlank(message = "La contraseña es obligatoria.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$", message = "La contraseña debe contener al menos una letra mayúscula y un número.")
    private String claveNueva;
}
