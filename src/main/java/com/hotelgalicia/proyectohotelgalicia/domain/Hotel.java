package com.hotelgalicia.proyectohotelgalicia.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El hotel debe tener un nombre.")
    @Size(min = 5, message = "Debe tener un minimo de 5 caracteres")
    private String nombre;

    @NotNull(message = "El hotel debe tener un nombre.")
    @Size(min = 10, message = "Debe tener un minimo de 10 caracteres")
    @Column(length = 5000)
    private String descripcion;

    private Municipios municipio;

    @NotEmpty(message = "El hotel debe tener una dirección registrada.")
    @NotNull(message = "El hotel debe tener una dirección registrada.")
    private String direccion;

    @Size(min = 9, message = "Debe tener un minimo de 9 caracteres")
    private String telefono;

    private String imagen;

    private Boolean estado;

    private double puntaje;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Empresa empresa;

    @ToString.Exclude
    @OneToMany(mappedBy = "hotel")
    private List<Habitacion> habitaciones = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "hotel")
    private List<Reserva> reservas = new ArrayList<>();

    @ToString.Exclude
    //EAGER Para hacer la carga con datos iniciales (Da problemas en el borrado)
    // @OneToMany(mappedBy = "libro",fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "hotel")
    private List<Valoracion> valoracion = new ArrayList<>();
}
