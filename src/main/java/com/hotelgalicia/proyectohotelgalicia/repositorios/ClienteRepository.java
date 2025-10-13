package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingIgnoreCase(String nom);

    @Query("SELECT c From Cliente c where CONCAT(LOWER(c.nombre),' ',LOWER(c.apellido)) LIKE CONCAT('%',LOWER(:nombre),'%')")
    List<Cliente> listByNameOrSurname(@Param("nombre") String nombre);

    List<Cliente> findByCorreoContainingIgnoreCase(String correo);
    
    Optional<Cliente> findByNombre(String nombre);

    Optional<Cliente> findByCorreoIgnoreCase(String correo);
}
