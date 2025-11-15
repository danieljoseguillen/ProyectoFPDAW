package com.hotelgalicia.proyectohotelgalicia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaDTOAdmin {
    @Email(message = "El correo debe tener un formato válido.")
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;

    @NotBlank(message = "La razón social es obligatoria.")
    private String razon;

    @NotBlank(message = "El CIF es obligatorio.")
    private String cif;
}
