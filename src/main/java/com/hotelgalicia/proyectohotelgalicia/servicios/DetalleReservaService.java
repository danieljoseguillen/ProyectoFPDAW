package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;

public interface DetalleReservaService {
    List<DetalleReserva> listByReserva(Long id);

    DetalleReserva agregar(DetalleReserva dreserv);

    DetalleReserva modificar(DetalleReserva dreserv);

    Boolean borrarPorId (Long habitacion, Long reserva);
}
