package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.domain.ValoracionID;

public interface ValoracionRepository extends JpaRepository<Valoracion, ValoracionID> {

    @Query("SELECT v FROM Valoracion v JOIN FETCH v.hotel WHERE v.cliente.correo = :correo")
    List<Valoracion> findByClienteCorreo(String correo);

    @Query("SELECT v FROM Valoracion v JOIN FETCH v.cliente WHERE v.hotel.id = :id")
    List<Valoracion> findByHotelId(@Param("id") Long id);

    @Query("SELECT v FROM Valoracion v JOIN FETCH v.hotel WHERE v.cliente.id = :id")
    List<Valoracion> findByClienteId(@Param("id") Long id);
}
