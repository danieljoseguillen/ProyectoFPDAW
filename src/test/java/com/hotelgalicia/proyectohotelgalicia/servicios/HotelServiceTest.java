package com.hotelgalicia.proyectohotelgalicia.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.EmptyListException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.FiltroBusqueda;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class HotelServiceTest {

    @Mock
    private UsuarioRepository uRep;

    @Mock
    private HotelRepository hoRep;

    @Mock
    private EmpresaRepository eRep;

    @Mock
    private FileStorageService fileserv;

    @Mock
    private DetalleReservaRepository drRep;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Empresa empresa;
    private Hotel hotel;
    private Habitacion habitacion;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setCorreo("corp@mail.com");

        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNombre("Suite Deluxe");
        habitacion.setPrecio(100d);
        habitacion.setCantidad(5);
        habitacion.setCapacidad(2);

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNombre("Hotel Galicia");
        hotel.setMunicipio(Municipios.A_CORUÑA);
        hotel.setDireccion("Av. Mar 1");
        hotel.setTelefono("987654321");
        hotel.setEmpresa(empresa);
        hotel.setEstado(true);
        hotel.setHabitaciones(List.of(habitacion));

        // Simula usuario autenticado
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("corp@mail.com");
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }

    @Test
    void testAgregarHotel_ok() {
        HotelDTO dto = new HotelDTO();
        dto.setNombre("Nuevo Hotel");
        dto.setDescripcion("Descripción del hotel.");
        dto.setDireccion("Calle A");
        dto.setTelefono("999888777");
        dto.setMunicipio(Municipios.A_CORUÑA);

        when(mockFile.isEmpty()).thenReturn(true);
        when(eRep.findByCorreo("corp@mail.com")).thenReturn(Optional.of(empresa));
        when(hoRep.save(any(Hotel.class))).thenAnswer(inv -> inv.getArgument(0));

        Hotel resultado = hotelService.agregar(dto, mockFile);

        assertNotNull(resultado);
        assertEquals("Nuevo Hotel", resultado.getNombre());
        verify(hoRep).save(any(Hotel.class));
    }

    @Test
    void testAgregarHotel_conImagen() {
        HotelDTO dto = new HotelDTO();
        dto.setNombre("Hotel con Imagen");
        dto.setDescripcion("Bonito hotel");
        dto.setDireccion("Calle Sol");
        dto.setTelefono("999888777");
        dto.setMunicipio(Municipios.LUGO);

        when(mockFile.isEmpty()).thenReturn(false);
        when(eRep.findByCorreo("corp@mail.com")).thenReturn(Optional.of(empresa));
        when(fileserv.store(mockFile, "Hotel con Imagen")).thenReturn("hotel.jpg");
        when(hoRep.save(any(Hotel.class))).thenAnswer(inv -> inv.getArgument(0));

        Hotel resultado = hotelService.agregar(dto, mockFile);
        assertEquals("hotel.jpg", resultado.getImagen());
    }

    @Test
    void testAgregarHotel_DataIntegrityError() {
        HotelDTO dto = new HotelDTO();
        dto.setNombre("Hotel Error");
        dto.setDescripcion("Descripción");
        dto.setDireccion("Calle Error");
        dto.setTelefono("123456789");
        dto.setMunicipio(Municipios.LUGO);

        when(eRep.findByCorreo("corp@mail.com")).thenReturn(Optional.of(empresa));
        when(hoRep.save(any())).thenThrow(new DataIntegrityViolationException("duplicado"));

        assertThrows(SaveFailedException.class, () -> hotelService.agregar(dto, mockFile));
    }

    @Test
    void testListSortedHotel_ok() {
        HotelSearchDTO dto = new HotelSearchDTO("", Municipios.TODOS, "", 2, 1, LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3), 50, 500, FiltroBusqueda.PRECIO_ASCENDENTE);
        // dto.setNombre("");
        // dto.setDireccion("");
        // dto.setMunicipio(Municipios.TODOS);
        // dto.setPersonas(2);
        // dto.setCantHabi(1);
        // dto.setFechaInicio(LocalDate.now().plusDays(1));
        // dto.setFechaFin(LocalDate.now().plusDays(3));
        // dto.setPresupuestoMin(50);
        // dto.setPresupuestoMax(500);
        // dto.setFiltro(FiltroBusqueda.PRECIO_ASCENDENTE);

        when(hoRep.findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCaseAndEstado(any(), any(), eq(true)))
                .thenReturn(List.of(hotel));

        when(drRep.sumByHabitacionId(anyLong(), anyList())).thenReturn(0);

        List<HotelMiniDTO> resultado = hotelService.listSortedHotel(dto);

        assertFalse(resultado.isEmpty());
        assertEquals("Hotel Galicia", resultado.get(0).getNombre());
    }

    @Test
    void testListSortedHotel_sinResultados() {
        HotelSearchDTO dto = new HotelSearchDTO("", Municipios.TODOS, "", 2, 1, LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3), 1000, 2000, FiltroBusqueda.PRECIO_ASCENDENTE);
        // dto.setNombre("");
        // dto.setDireccion("");
        // dto.setMunicipio(Municipios.TODOS);
        // dto.setPersonas(2);
        // dto.setCantHabi(1);
        // dto.setFechaInicio(LocalDate.now().plusDays(1));
        // dto.setFechaFin(LocalDate.now().plusDays(3));
        // dto.setPresupuestoMin(1000);
        // dto.setPresupuestoMax(2000);
        // dto.setFiltro(FiltroBusqueda.PRECIO_ASCENDENTE);

        when(hoRep.findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCaseAndEstado(any(), any(), eq(true)))
                .thenReturn(List.of(hotel));

        assertThrows(EmptyListException.class, () -> hotelService.listSortedHotel(dto));
    }

    @Test
    void testCambiarEstadoPorId_ok() {
        when(hoRep.findById(1L)).thenReturn(Optional.of(hotel));
        when(uRep.findByCorreo(any())).thenReturn(Optional.of(new Usuario()));
        when(hoRep.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Hotel actualizado = hotelService.cambiarEstadoPorId(1L, false);
        assertFalse(actualizado.getEstado());
    }
}