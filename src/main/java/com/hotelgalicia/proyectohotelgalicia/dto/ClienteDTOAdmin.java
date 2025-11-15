package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTOAdmin {
    @Email(message = "El correo debe tener un formato válido.")
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;

    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
    @NotBlank(message = "El usuario debe tener un nombre.")
    private String nombre;

    @Size(min = 3, message = "El apellido debe tener al menos 3 caracteres.")
    @NotBlank(message = "Debe agregar un apellido.")
    private String apellido;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefono;
}
