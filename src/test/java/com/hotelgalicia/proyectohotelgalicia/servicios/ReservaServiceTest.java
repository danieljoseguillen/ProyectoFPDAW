package com.hotelgalicia.proyectohotelgalicia.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
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
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ReservaServiceTest {

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

    @Mock
    private SecurityContext context;
    @Mock
    private Authentication auth;

    @InjectMocks
    private ReservaServiceImpl service;

    private Cliente cliente;
    private Hotel hotel;
    private Habitacion hab;
    private Reserva reserva;
    private Usuario user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(auth);

        cliente = Cliente.builder().id(1L).correo("user@demo.com").build();
        hotel = Hotel.builder().id(10L).nombre("Hotel Galicia").build();
        hab = Habitacion.builder().id(100L).nombre("Suite Premium").cantidad(5).precio(120.0).build();

        reserva = Reserva.builder()
                .fechaInicio(LocalDate.now().plusDays(2))
                .fechaFin(LocalDate.now().plusDays(5))
                .personas(2)
                .cliente(cliente)
                .hotel(hotel)
                .estado(EstadoReserva.REALIZADA)
                .build();

        user = Usuario.builder().id(1L).correo("user@demo.com").rol(Roles.USER).build();
    }

    // ======================================================
    // HELPERS
    // ======================================================

    private void mockAuth(String correo, Roles rol) {
        when(auth.getName()).thenReturn(correo);
        when(uRep.findByCorreo(correo))
                .thenReturn(Optional.of(Usuario.builder().id(1L).correo(correo).rol(rol).build()));
    }

    private ReservaDTO mockReservaDTO() {
        DetalleReservaDTO d = new DetalleReservaDTO();
        d.setHabitacion(100L);
        d.setCantidad(2);

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));
        dto.setPersonas(2);
        dto.setHabitaciones(List.of(d));
        return dto;
    }

    // ======================================================
    // TESTS
    // ======================================================

    // @Test
    // void agregar_ok() {
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(cRep.findByCorreoIgnoreCase(any())).thenReturn(Optional.of(cliente));
    //     when(hoRep.findById(10L)).thenReturn(Optional.of(hotel));
    //     when(haRep.findById(100L)).thenReturn(Optional.of(hab));
    //     when(drRep.sumByHabitacionId(any(), any())).thenReturn(0);
    //     when(reRep.save(any())).thenReturn(reserva);

    //     ReservaDTO dto = mockReservaDTO();
    //     Reserva result = service.agregar(dto, 10L);

    //     assertNotNull(result);
    //     verify(reRep).save(any());
    // }

    // @Test
    // void agregar_habitacionNoDisponible() {
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(cRep.findByCorreoIgnoreCase(any())).thenReturn(Optional.of(cliente));
    //     when(hoRep.findById(10L)).thenReturn(Optional.of(hotel));
    //     when(haRep.findById(100L)).thenReturn(Optional.of(hab));
    //     when(drRep.sumByHabitacionId(any(), any())).thenReturn(5); // total igual a capacidad

    //     ReservaDTO dto = mockReservaDTO();
    //     assertThrows(RoomFullException.class, () -> service.agregar(dto, 10L));
    // }

    // @Test
    // void modificar_ok() {
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(reRep.findById(99L)).thenReturn(Optional.of(reserva));
    //     when(uRep.findByCorreo(any())).thenReturn(Optional.of(user));
    //     when(cRep.findById(1L)).thenReturn(Optional.of(cliente));
    //     when(haRep.findById(100L)).thenReturn(Optional.of(hab));
    //     when(drRep.sumByHabitacionId(any(), any())).thenReturn(0);
    //     when(reRep.save(any())).thenReturn(reserva);

    //     ReservaDTO dto = mockReservaDTO();
    //     Reserva result = service.modificar(dto, 99L);
    //     assertEquals(reserva.getCliente(), result.getCliente());
    // }

    // @Test
    // void cambiarEstado_ok() {
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(reRep.findById(99L)).thenReturn(Optional.of(reserva));
    //     when(uRep.findByCorreo(any())).thenReturn(Optional.of(user));
    //     when(cRep.findById(1L)).thenReturn(Optional.of(cliente));
    //     when(reRep.save(any())).thenReturn(reserva);

    //     EstadoReservaDTO dto = new EstadoReservaDTO();
    //     dto.setId(99L);
    //     dto.setEstado(EstadoReserva.CONFIRMADA);

    //     Reserva result = service.cambiarEstado(dto);
    //     assertEquals(EstadoReserva.CONFIRMADA, result.getEstado());
    // }

    // @Test
    // void cancelar_ok() {
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(reRep.findById(99L)).thenReturn(Optional.of(reserva));
    //     when(uRep.findByCorreo(any())).thenReturn(Optional.of(user));
    //     when(cRep.findById(1L)).thenReturn(Optional.of(cliente));
    //     when(reRep.save(any())).thenReturn(reserva);

    //     assertTrue(service.cancelarPorId(99L));
    //     verify(reRep).save(any());
    // }

    // @Test
    // void cancelar_conMenosDe24Horas_lanzaExcepcion() {
    //     reserva.setFechaInicio(LocalDate.now());
    //     when(reRep.findById(99L)).thenReturn(Optional.of(reserva));
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(uRep.findByCorreo(any())).thenReturn(Optional.of(user));
    //     when(cRep.findById(1L)).thenReturn(Optional.of(cliente));

    //     assertThrows(RuntimeException.class, () -> service.cancelarPorId(99L));
    // }

    // @Test
    // void agregar_fallaAlGuardar_lanzaSaveFailed() {
    //     mockAuth("user@demo.com", Roles.USER);
    //     when(cRep.findByCorreoIgnoreCase(any())).thenReturn(Optional.of(cliente));
    //     when(hoRep.findById(any())).thenReturn(Optional.of(hotel));
    //     when(haRep.findById(any())).thenReturn(Optional.of(hab));
    //     when(drRep.sumByHabitacionId(any(), any())).thenReturn(0);
    //     when(reRep.save(any())).thenThrow(new DataIntegrityViolationException("duplicado"));

    //     assertThrows(SaveFailedException.class, () -> service.agregar(mockReservaDTO(), 10L));
    // }
}