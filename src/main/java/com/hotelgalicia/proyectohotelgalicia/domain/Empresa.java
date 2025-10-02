package com.hotelgalicia.proyectohotelgalicia.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class Empresa extends Usuario {

    @NotBlank(message = "La razón social es obligatoria.")
    private String razonSocial;

    @NotBlank(message = "El CIF es obligatorio.")
    @Column(unique = true, nullable = false)
    private String cif;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "empresa")
    @Builder.Default
    private List<Hotel> hoteles = new ArrayList<>();
}