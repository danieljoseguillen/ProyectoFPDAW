package com.hotelgalicia.proyectohotelgalicia.servicios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaListDTO;

public interface ReservaService {

    Page<ReservaListDTO> listarReservasCliente(Long id, Pageable pageable);

    Page<ReservaListDTO> listarReservasHotel(Long id, Pageable pageable);

    Reserva getById(Long id);

    Reserva agregar(ReservaDTO reserv, Long id);

    Reserva modificar(ReservaDTO reserv, Long id);

    Reserva cambiarEstado(EstadoReservaDTO estado);

    Boolean cancelarPorId(Long id);

    void verificarReserva(Reserva reserva);

    boolean verificarCantidad(ReservaDTO reserva);

    Double calcularPrecioTotal(ReservaDTO reserva);

    Double calcularPrecioTotal(Reserva reserva);

}
