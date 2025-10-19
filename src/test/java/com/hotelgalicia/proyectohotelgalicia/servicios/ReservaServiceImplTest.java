package com.hotelgalicia.proyectohotelgalicia.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.dto.DetalleReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.RoomFullException;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

class ReservaServiceImplTest {
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private ReservaRepository reRep;
    @Mock
    private DetalleReservaRepository drRep;
    @Mock
    private HotelRepository hoRep;
    @Mock
    private HabitacionRepository haRep;
    @Mock
    private ClienteRepository cRep;
    @Mock
    private UsuarioRepository uRep;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@correo.com");
    }

    @Test
    void agregarReservaDatosCorrectos() {
        Cliente cliente = new Cliente(); cliente.setId(1L);
        Hotel hotel = new Hotel(); hotel.setId(1L);
        Habitacion habitacion = new Habitacion(); habitacion.setId(1L); habitacion.setCantidad(5); habitacion.setNombre("Suite"); habitacion.setPrecio(100.0);
        DetalleReservaDTO detalle = new DetalleReservaDTO(); detalle.setHabitacion(1L); detalle.setCantidad(2);
        ReservaDTO reservaDTO = new ReservaDTO(); reservaDTO.setFechaInicio(LocalDate.now()); reservaDTO.setFechaFin(LocalDate.now().plusDays(1)); reservaDTO.setPersonas(2); reservaDTO.setHabitaciones(List.of(detalle));
        when(cRep.findByCorreoIgnoreCase(anyString())).thenReturn(Optional.of(cliente));
        when(hoRep.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(haRep.findById(anyLong())).thenReturn(Optional.of(habitacion));
        when(drRep.sumByHabitacionId(anyLong(),List.of(EstadoReserva.REALIZADA, EstadoReserva.CONFIRMADA))).thenReturn(1);
        when(reRep.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));
        Reserva reserva = reservaService.agregar(reservaDTO, 1L);
        assertNotNull(reserva);
        assertEquals(EstadoReserva.REALIZADA, reserva.getEstado());
    }

    @Test
    void agregarReservaMasQueDisponibles() {
        Cliente cliente = new Cliente(); cliente.setId(1L);
        Hotel hotel = new Hotel(); hotel.setId(1L);
        Habitacion habitacion = new Habitacion(); habitacion.setId(1L); habitacion.setCantidad(2); habitacion.setNombre("Suite"); habitacion.setPrecio(100.0);
        DetalleReservaDTO detalle = new DetalleReservaDTO(); detalle.setHabitacion(1L); detalle.setCantidad(3);
        ReservaDTO reservaDTO = new ReservaDTO(); reservaDTO.setFechaInicio(LocalDate.now()); reservaDTO.setFechaFin(LocalDate.now().plusDays(1)); reservaDTO.setPersonas(2); reservaDTO.setHabitaciones(List.of(detalle));
        when(cRep.findByCorreoIgnoreCase(anyString())).thenReturn(Optional.of(cliente));
        when(hoRep.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(haRep.findById(anyLong())).thenReturn(Optional.of(habitacion));
        when(drRep.sumByHabitacionId(anyLong(),List.of(EstadoReserva.REALIZADA, EstadoReserva.CONFIRMADA))).thenReturn(0);
        assertThrows(RoomFullException.class, () -> reservaService.agregar(reservaDTO, 1L));
    }

    @Test
    void agregarReservaDatosErrados() {
        ReservaDTO reservaDTO = new ReservaDTO(); reservaDTO.setFechaInicio(LocalDate.now()); reservaDTO.setFechaFin(LocalDate.now().plusDays(1)); reservaDTO.setPersonas(2); reservaDTO.setHabitaciones(new ArrayList<>());
        when(cRep.findByCorreoIgnoreCase(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reservaService.agregar(reservaDTO, 1L));
    }
}
