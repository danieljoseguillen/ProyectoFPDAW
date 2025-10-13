package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.domain.ValoracionID;

public interface ValoracionRepository extends JpaRepository<Valoracion, ValoracionID> {

    List<Valoracion> findByHotelId(Long id);
    List<Valoracion> findByClienteCorreo(String correo);
}
