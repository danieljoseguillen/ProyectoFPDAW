package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;

public interface ReservaService {

    List<Reserva> listByCliente(Long id);

    List<Reserva> listByHotel(Long id);
    
    Reserva getById(Long id);

    Reserva agregar (ReservaDTO reserv, Long id);
    
    Reserva modificar (ReservaDTO reserv, Long id);

    Reserva cambiarEstado(EstadoReservaDTO estado);

    Boolean cancelarPorId (Long id);

    void verificarReserva(Reserva reserva);

}
