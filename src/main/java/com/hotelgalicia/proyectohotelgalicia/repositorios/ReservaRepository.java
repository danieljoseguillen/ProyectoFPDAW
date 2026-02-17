package com.hotelgalicia.proyectohotelgalicia.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

        @Query("SELECT DISTINCT r FROM Reserva r " +
                        "JOIN FETCH r.hotel " +
                        "JOIN FETCH r.cliente " +
                        "LEFT JOIN FETCH r.habitaciones d " +
                        "LEFT JOIN FETCH d.habitacion hab " +
                        "WHERE r.cliente.id = :id ORDER BY r.fechaInicio DESC")
        Page<Reserva> findByClienteId(Long id, Pageable pageable);

        @Query("SELECT DISTINCT r FROM Reserva r " +
                        "JOIN FETCH r.hotel " +
                        "JOIN FETCH r.cliente " +
                        "LEFT JOIN FETCH r.habitaciones d " +
                        "LEFT JOIN FETCH d.habitacion hab " +
                        "WHERE r.hotel.id = :id ORDER BY r.fechaInicio DESC")
        Page<Reserva> findByHotelId(Long id, Pageable pageable);
}
