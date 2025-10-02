package com.hotelgalicia.proyectohotelgalicia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;
import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReservaId;

public interface DetalleReservaRepository extends JpaRepository<DetalleReserva,DetalleReservaId>{

}
