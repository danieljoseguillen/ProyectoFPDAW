package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByHotelId(Long id);

    List<Habitacion> findByEstado(EstadoHabitacion estado);

    List<Habitacion> findByHotelIdAndEstado(Long id, EstadoHabitacion estado);

    @Query("SELECT MAX(h.precio) FROM Habitacion h WHERE h.estado = :estado")
    Double findMaxPrecio(@Param("estado") EstadoHabitacion estado);

    @Query("SELECT MIN(h.precio) FROM Habitacion h WHERE h.estado = :estado")
    Double findMinPrecio(@Param("estado") EstadoHabitacion estado);
}
