package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaDTO {
    @Email(message = "El correo debe tener un formato válido.")
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @NotBlank(message = "La contraseña es obligatoria.")
    private String contraseña;

    @NotBlank(message = "La razón social es obligatoria.")
    private String razon;

    @NotBlank(message = "El CIF es obligatorio.")
    private String cif;
}
