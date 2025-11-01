package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByCorreoContainingIgnoreCase(String correo);

    List<Empresa> findByRazonSocialContainingIgnoreCase(String razon);

    Optional<Empresa> findByRazonSocialIgnoreCase(String razon);

    Optional<Empresa> findByCorreo(String correo);

    Optional<Empresa> findByCorreoIgnoreCase(String correo);
}
