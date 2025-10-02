package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;

public interface ReservaService {

    List<Reserva> listByCliente(Long id);

    List<Reserva> listByHotel(Long id);

    Reserva agregar (Reserva reserv);
    
    Reserva modificar (Reserva reserv);

    Reserva cambiarEstado(EstadoReservaDTO estado);
}
