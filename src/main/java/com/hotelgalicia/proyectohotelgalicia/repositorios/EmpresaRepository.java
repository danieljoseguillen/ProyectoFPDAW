package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Page<Empresa> findByCorreoContainingIgnoreCase(String correo, Pageable pageable);

    Page<Empresa> findByRazonSocialContainingIgnoreCase(String razon, Pageable pageable);

    Optional<Empresa> findByRazonSocialIgnoreCase(String razon);

    Optional<Empresa> findByCorreo(String correo);

    Optional<Empresa> findByCorreoIgnoreCase(String correo);
}
