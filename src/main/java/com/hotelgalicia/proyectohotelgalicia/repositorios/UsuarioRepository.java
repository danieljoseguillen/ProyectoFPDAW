package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    Boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u LEFT JOIN Cliente c ON u.id = c.id LEFT JOIN Empresa e ON u.id = e.id WHERE u.correo = :correo")
    Optional<Usuario> findFullUsuarioByCorreo(@Param("correo") String correo);

}
