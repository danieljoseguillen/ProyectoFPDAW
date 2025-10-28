package com.hotelgalicia.proyectohotelgalicia.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository eRep;
    @Mock
    private UsuarioRepository uRep;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private SecurityContext context;
    @Mock
    private Authentication auth;

    @InjectMocks
    private EmpresaServiceImpl service;

    private Empresa empresa;
    private EmpresaDTO dto;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(auth);

        empresa = Empresa.builder()
                .id(1L)
                .correo("empresa@demo.com")
                .contraseña("encodedPass")
                .razonSocial("Empresa Demo")
                .cif("CIF123")
                .rol(Roles.CORPORATION)
                .estado(true)
                .build();

        dto = new EmpresaDTO();
        dto.setCorreo("empresa@demo.com");
        dto.setContraseña("1234");
        dto.setRazon("Empresa Demo");
        dto.setCif("CIF123");

    }

    // ======================================================
    // TESTS
    // ======================================================

    @Test
    void agregar_ok() {
        when(uRep.findByCorreo(any())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("encodedPass");
        when(eRep.save(any())).thenReturn(empresa);

        Empresa result = service.agregar(dto);

        assertEquals("empresa@demo.com", result.getCorreo());
        verify(eRep).save(any());
    }

    @Test
    void agregar_correoEnUso_lanzaExcepcion() {
        when(uRep.findByCorreo(any())).thenReturn(Optional.of(empresa));
        assertThrows(RuntimeException.class, () -> service.agregar(dto));
    }

    @Test
    void agregar_errorAlGuardar_lanzaSaveFailed() {
        when(uRep.findByCorreo(any())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("encoded");
        when(eRep.save(any())).thenThrow(new DataIntegrityViolationException("duplicado"));
        assertThrows(SaveFailedException.class, () -> service.agregar(dto));
    }

    @Test
    void modificar_ok() {
        when(eRep.findById(1L)).thenReturn(Optional.of(empresa));
        mockAuthAs("empresa@demo.com", Roles.CORPORATION);
        when(encoder.matches("1234", "encodedPass")).thenReturn(true);
        when(eRep.save(any())).thenReturn(empresa);

        Empresa result = service.modificar(dto, 1L);
        assertEquals("empresa@demo.com", result.getCorreo());
    }

    @Test
    void modificar_contraseñaIncorrecta() {
        when(eRep.findById(1L)).thenReturn(Optional.of(empresa));
        mockAuthAs("empresa@demo.com", Roles.CORPORATION);
        when(encoder.matches("1234", "encodedPass")).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> service.modificar(dto, 1L));
    }

    @Test
    void cambiarEstado_admin_ok() {
        when(eRep.findById(1L)).thenReturn(Optional.of(empresa));
        mockAuthAs("admin@demo.com", Roles.ADMIN);
        when(eRep.save(any())).thenReturn(empresa);

        assertTrue(service.cambiarEstadoPorId(1L, false));
    }

    @Test
    void cambiarEstado_noAdmin_lanzaExcepcion() {
        when(eRep.findById(1L)).thenReturn(Optional.of(empresa));
        mockAuthAs("empresa@demo.com", Roles.CORPORATION);
        assertThrows(PermissionDeniedException.class, () -> service.cambiarEstadoPorId(1L, false));
    }

    @Test
    void cambiarContraseña_ok() {
        when(eRep.findById(1L)).thenReturn(Optional.of(empresa));
        mockAuthAs("empresa@demo.com", Roles.CORPORATION);
        when(encoder.matches("actual", "encodedPass")).thenReturn(true);
        when(encoder.matches("nueva", "encodedPass")).thenReturn(false);
        when(encoder.encode("nueva")).thenReturn("encodedNueva");

        boolean result = service.cambiarContraseñaPorId(1L, mockClave("actual", "nueva"));

        assertTrue(result);
        verify(eRep).save(any());
    }

    @Test
    void cambiarContraseña_incorrecta() {
        when(eRep.findById(1L)).thenReturn(Optional.of(empresa));
        mockAuthAs("empresa@demo.com", Roles.CORPORATION);
        when(encoder.matches("actual", "encodedPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> service.cambiarContraseñaPorId(1L, mockClave("actual", "nueva")));
    }

    @Test
    void listAll_ok() {
        when(eRep.findAll()).thenReturn(List.of(empresa));
        assertEquals(1, service.listAll().size());
    }

    @Test
    void getByCorreo_noEncontrado() {
        when(eRep.findByCorreo(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getByCorreo("x@demo.com"));
    }

    // ======================================================
    // HELPERS
    // ======================================================

    private void mockAuthAs(String correo, Roles rol) {
        when(auth.getName()).thenReturn(correo);
        Usuario user = Usuario.builder().correo(correo).rol(rol).id(
                rol == Roles.CORPORATION ? 1L : 99L).build();
        when(uRep.findByCorreo(correo)).thenReturn(Optional.of(user));
    }

    private ClaveDTO mockClave(String actual, String nueva) {
        ClaveDTO clave = new ClaveDTO(actual, nueva);
        return clave;
    }
}
