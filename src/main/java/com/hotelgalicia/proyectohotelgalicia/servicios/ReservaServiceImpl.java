package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.DetalleReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.RoomFullException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reRep;

    @Autowired
    private DetalleReservaRepository drRep;

    @Autowired
    private HotelRepository hoRep;

    @Autowired
    private HabitacionRepository haRep;

    @Autowired
    private ClienteRepository cRep;

    @Autowired
    private UsuarioRepository uRep;

    @Override
    public List<Reserva> listByCliente(Long id) {
        return reRep.findByClienteId(id);
    }

    @Override
    public List<Reserva> listByHotel(Long id) {
        return reRep.findByHotelId(id);
    }

    @Override
    public Reserva getById(Long id) {
        return reRep.findById(id).orElseThrow(() -> new RuntimeException("Error: Reserva no encontrada."));
    }

    @Override
    @Transactional
    public Reserva agregar(ReservaDTO reserv, Long hotelId) {
        String correo = "carlos.perez@example.com";
        // SecurityContextHolder.getContext().getAuthentication().getName();
        // Identifica hotel y cliente
        Cliente cliente = cRep.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        Hotel hotel = hoRep.findById(hotelId).orElseThrow(() -> new RuntimeException("Error: Hotel no encontrado"));

        Reserva reservaFinal = new Reserva(null, reserv.getFechaInicio(),
                reserv.getFechaFin(), reserv.getPersonas(), EstadoReserva.REALIZADA, cliente, hotel, new ArrayList<>());

        // Verificar duplicados en los detalles
        Set<Long> idsHabitaciones = new HashSet<>();
        for (DetalleReservaDTO detalle : reserv.getHabitaciones()) {
            if (!idsHabitaciones.add(detalle.getHabitacion())) {
                throw new RuntimeException(
                        "Error: Hay habitaciones duplicadas en la reserva (ID: " + detalle.getHabitacion() + ")");
            }
        }

        try {
            // Agregar detalles de reserva
            for (DetalleReservaDTO detalle : reserv.getHabitaciones()) {
                if (detalle.getCantidad() < 1)
                    continue;
                Habitacion habi = haRep.findById(detalle.getHabitacion())
                        .orElseThrow(() -> new RuntimeException("Error: Habitacion no encontrada"));
                verificarDisponibilidad(habi, detalle.getCantidad());
                DetalleReserva detFinal = new DetalleReserva(habi, reservaFinal, detalle.getCantidad(),
                        habi.getNombre(), habi.getPrecio());
                // drRep.save(detFinal);
                reservaFinal.getHabitaciones().add(detFinal);
            }
            Reserva refinal = reRep.save(reservaFinal);
            return refinal;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al realizar la reserva: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al realizar la reserva: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Reserva modificar(ReservaDTO reserv, Long id) {
        Reserva reservaFinal = reRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));

        // verificarReserva(reservaFinal);
        reservaFinal.setFechaInicio(reserv.getFechaInicio());
        reservaFinal.setFechaFin(reserv.getFechaFin());
        reservaFinal.setPersonas(reserv.getPersonas());

        drRep.deleteAll(reservaFinal.getHabitaciones());

        reservaFinal.getHabitaciones().clear();
        // reRep.flush();
        // Verificar duplicados en los detalles
        Set<Long> idsHabitaciones = new HashSet<>();
        for (DetalleReservaDTO detalle : reserv.getHabitaciones()) {
            if (!idsHabitaciones.add(detalle.getHabitacion())) {
                throw new RuntimeException(
                        "Error: Hay habitaciones duplicadas en la reserva (ID: " + detalle.getHabitacion() + ")");
            }
        }
        try {
            // Agregar detalles de reserva
            for (DetalleReservaDTO detalle : reserv.getHabitaciones()) {
                if (detalle.getCantidad() < 1)
                    continue;
                Habitacion habi = haRep.findById(detalle.getHabitacion())
                        .orElseThrow(() -> new RuntimeException("Error: Habitacion no encontrada"));
                verificarDisponibilidad(habi, detalle.getCantidad());
                DetalleReserva detFinal = new DetalleReserva(habi, reservaFinal, detalle.getCantidad(),
                        habi.getNombre(), habi.getPrecio());
                // drRep.save(detFinal);
                reservaFinal.getHabitaciones().add(detFinal);
            }
            Reserva refinal = reRep.save(reservaFinal);
            return refinal;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al modificar la reserva: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al modificar la reserva: " + e.getMessage());
        }
    }

    @Override
    public Reserva cambiarEstado(EstadoReservaDTO estado) {
        Reserva reserva = reRep.findById(estado.getId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));
        verificarReserva(reserva);
        reserva.setEstado(estado.getEstado());
        try {
            return reRep.save(reserva);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al guardar los cambios: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public Boolean cancelarPorId(Long id) {
        Reserva reserva = reRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));
        verificarReserva(reserva);
        if (!reserva.getFechaInicio().isAfter(LocalDate.now().plusDays(1))) {
            throw new RuntimeException("Solo pueden cancelarse reservas con más de 1 día de antelación.");
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        try {
            reRep.save(reserva);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al cancelar la reserva: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al cancelar la reserva: " + e.getMessage());
        }
    }

    public void verificarReserva(Reserva reserva) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));

        switch (usuario.getRol()) {
            case ADMIN -> {
            }
            case USER -> {
                Cliente cliente = cRep.findById(usuario.getId())
                        .orElseThrow(
                                () -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
                if (!reserva.getCliente().getId().equals(cliente.getId())) {
                    throw new RuntimeException("No posee permisos para modificar la reserva.");
                }
            }
            case CORPORATION -> {
                Hotel hotel = reserva.getHotel();
                if (!hotel.getEmpresa().getId().equals(usuario.getId()))
                    throw new RuntimeException("No posee permisos para realizar dicha acción.");
            }
            default -> throw new RuntimeException("Error al verificar permisos de usuario.");
        }
    }

    @Override
    public boolean verificarCantidad(ReservaDTO reserva){
        return reserva.getHabitaciones().stream()
        .anyMatch(h -> h.getCantidad() != null && h.getCantidad() > 0);
    }

    private void verificarDisponibilidad(Habitacion habitacion, int cantSoli) {
        Integer cantReserv = drRep.sumByHabitacionId(habitacion.getId(),
                List.of(EstadoReserva.REALIZADA, EstadoReserva.CONFIRMADA));
        if (cantReserv == null) {
            cantReserv = 0;
        }
        int disponible = habitacion.getCantidad() - cantReserv;
        if (!(disponible >= cantSoli)) {
            throw new RoomFullException(habitacion.getNombre(), disponible, cantSoli);
        }
    }
}
