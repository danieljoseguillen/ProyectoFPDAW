package com.hotelgalicia.proyectohotelgalicia.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.domain.ValoracionID;

public interface ValoracionRepository extends JpaRepository<Valoracion, ValoracionID> {

    @Query("SELECT v FROM Valoracion v JOIN FETCH v.hotel WHERE v.cliente.correo = :correo ORDER BY v.fecha DESC")
    Page<Valoracion> findByClienteCorreo(String correo, Pageable pageable);

    @Query("SELECT v FROM Valoracion v JOIN FETCH v.cliente WHERE v.hotel.id = :id ORDER BY v.fecha DESC")
    Page<Valoracion> findByHotelId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT v FROM Valoracion v JOIN FETCH v.hotel WHERE v.cliente.id = :id ORDER BY v.fecha DESC")
    Page<Valoracion> findByClienteId(@Param("id") Long id, Pageable pageable);
}
