package com.hotelgalicia.proyectohotelgalicia.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class HabitacionServiceTest {

    @Mock
    private HotelRepository hoRep;

    @Mock
    private HabitacionRepository haRep;

    @Mock
    private DetalleReservaRepository drRep;

    @Mock
    private FileStorageService fileserv;

    @Mock
    private HotelService hoServ;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private HabitacionServiceImpl service;

    private Hotel hotel;
    private Habitacion habitacion;
    private HabitacionDTO dto;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNombre("Hotel Demo");

        habitacion = new Habitacion();
        habitacion.setId(10L);
        habitacion.setNombre("Suite");
        habitacion.setDescripcion("Suite premium");
        habitacion.setCantidad(5);
        habitacion.setCapacidad(2);
        habitacion.setPrecio(100d);
        habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
        habitacion.setHotel(hotel);

        dto = new HabitacionDTO();
        dto.setNombre("Suite");
        dto.setDescripcion("Suite premium");
        dto.setCantidad(5);
        dto.setCapacidad(2);
        dto.setPrecio(100d);
    }

    // @Test
    // void listHabitacionByHotelId_OK() {
    //     when(haRep.findByHotelIdAndEstado(1L, EstadoHabitacion.DISPONIBLE))
    //             .thenReturn(List.of(habitacion));
    //     when(drRep.sumByHabitacionId(anyLong(), anyList())).thenReturn(1);

    //     var result = service.listHabitacionByHotelId(1L);
    //     assertEquals(1, result.size());
    //     assertEquals("Suite", result.get(0).getNombre());
    //     assertEquals(4, result.get(0).getDisponibles()); // cantidad 5 - 1 reservada
    // }

    @Test
    void getById_OK() {
        when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
        Habitacion result = service.getById(10L);
        assertEquals("Suite", result.getNombre());
    }

    @Test
    void getById_NotFound() {
        when(haRep.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(99L));
    }

    @Test
    void agregar_OK_SinImagen() {
        when(hoRep.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hoServ).verificarHotel(any());
        when(file.isEmpty()).thenReturn(true);
        when(haRep.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Habitacion result = service.agregar(dto, 1L, file);
        assertEquals("Suite", result.getNombre());
        verify(haRep).save(any());
    }

    @Test
    void agregar_OK_ConImagen() {
        when(hoRep.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hoServ).verificarHotel(any());
        when(file.isEmpty()).thenReturn(false);
        when(fileserv.store(file, "Hotel Demo_hab")).thenReturn("hab.jpg");
        when(haRep.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Habitacion result = service.agregar(dto, 1L, file);
        assertEquals("hab.jpg", result.getImagen());
    }

    @Test
    void agregar_ErrorEnSave() {
        when(hoRep.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hoServ).verificarHotel(any());
        when(file.isEmpty()).thenReturn(true);
        when(haRep.save(any())).thenThrow(new DataIntegrityViolationException("duplicado"));

        assertThrows(SaveFailedException.class, () -> service.agregar(dto, 1L, file));
    }

    @Test
    void modificar_OK() {
        when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
        doNothing().when(hoServ).verificarHotel(any());
        when(file.isEmpty()).thenReturn(true);
        when(haRep.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Habitacion result = service.modificar(dto, 10L, file);
        assertEquals("Suite", result.getNombre());
    }

    // @Test
    // void cambiarEstado_OK() {
    //     when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
    //     doNothing().when(hoServ).verificarHotel(any());
    //     when(haRep.save(any())).thenAnswer(inv -> inv.getArgument(0));

    //     EstadoHabitacionDTO estadoDTO = new EstadoHabitacionDTO();
    //     estadoDTO.setId(10L);
    //     estadoDTO.setEstado(EstadoHabitacion.INACTIVA);

    //     Habitacion result = service.cambiarEstado(estadoDTO);
    //     assertEquals(EstadoHabitacion.INACTIVA, result.getEstado());
    // }

    @Test
    void desabilitarPorId_OK() {
        when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
        doNothing().when(hoServ).verificarHotel(any());
        when(haRep.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Habitacion result = service.DesabilitarPorId(10L);
        assertEquals(EstadoHabitacion.ELIMINADA, result.getEstado());
    }

        @Test
    void agregar_ErrorVerificarHotel() {
        when(hoRep.findById(1L)).thenReturn(Optional.of(hotel));
        doThrow(new RuntimeException("Permiso denegado")).when(hoServ).verificarHotel(any());

        assertThrows(RuntimeException.class, () -> service.agregar(dto, 1L, file));
    }

    @Test
    void modificar_ErrorVerificarHotel() {
        when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
        doThrow(new RuntimeException("Permiso denegado")).when(hoServ).verificarHotel(any());

        assertThrows(RuntimeException.class, () -> service.modificar(dto, 10L, file));
    }

    @Test
    void agregar_ErrorArchivo() {
        when(hoRep.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hoServ).verificarHotel(any());
        when(file.isEmpty()).thenReturn(false);
        when(fileserv.store(file, "Hotel Demo_hab")).thenThrow(new RuntimeException("Error al guardar archivo"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.agregar(dto, 1L, file));
        assertTrue(ex.getMessage().contains("Error al guardar archivo"));
    }

    @Test
    void modificar_ErrorArchivo() {
        when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
        doNothing().when(hoServ).verificarHotel(any());
        when(file.isEmpty()).thenReturn(false);
        habitacion.setImagen("antigua.jpg");
        when(fileserv.store(file, "Hotel Demo_hab")).thenThrow(new RuntimeException("Error al guardar archivo"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.modificar(dto, 10L, file));
        assertTrue(ex.getMessage().contains("Error al guardar la imagen"));
    }

    // @Test
    // void cambiarEstado_ErrorSave() {
    //     when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
    //     doNothing().when(hoServ).verificarHotel(any());
    //     when(haRep.save(any())).thenThrow(new DataIntegrityViolationException("duplicado"));

    //     EstadoHabitacionDTO estadoDTO = new EstadoHabitacionDTO();
    //     estadoDTO.setId(10L);
    //     estadoDTO.setEstado(EstadoHabitacion.INACTIVA);

    //     assertThrows(SaveFailedException.class, () -> service.cambiarEstado(estadoDTO));
    // }

    @Test
    void desabilitarPorId_ErrorSave() {
        when(haRep.findById(10L)).thenReturn(Optional.of(habitacion));
        doNothing().when(hoServ).verificarHotel(any());
        when(haRep.save(any())).thenThrow(new DataIntegrityViolationException("duplicado"));

        assertThrows(SaveFailedException.class, () -> service.DesabilitarPorId(10L));
    }
}