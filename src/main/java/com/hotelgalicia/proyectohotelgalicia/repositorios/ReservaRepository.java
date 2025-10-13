package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{
    List<Reserva> findByClienteId(Long id);
    List<Reserva> findByHotelId(Long id);
}
