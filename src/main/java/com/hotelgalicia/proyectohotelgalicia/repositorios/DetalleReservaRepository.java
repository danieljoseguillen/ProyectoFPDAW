package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;
import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReservaId;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;

public interface DetalleReservaRepository extends JpaRepository<DetalleReserva, DetalleReservaId> {
    @Query("SELECT  COALESCE(SUM(dr.cantidad),0) FROM DetalleReserva dr JOIN dr.reserva re WHERE dr.habitacion.id = :id AND  re.estado IN :estados")
    Integer sumByHabitacionId(@Param("id") Long id, @Param("estados") List<EstadoReserva> estados);

}
