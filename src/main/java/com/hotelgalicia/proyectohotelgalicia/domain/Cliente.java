package com.hotelgalicia.proyectohotelgalicia.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class Cliente extends Usuario {

    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
    @NotBlank(message = "El usuario debe tener un nombre.")
    private String nombre;

     @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
    @NotBlank(message = "Debe agregar un apellido.")
    private String apellido;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefono;
}
