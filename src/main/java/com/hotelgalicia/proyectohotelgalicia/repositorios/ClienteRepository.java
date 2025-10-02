package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingIgnoreCase(String nom);

    Optional<Cliente> findByNombre(String nombre);

    Boolean existsByNombre(String nombre);
}
