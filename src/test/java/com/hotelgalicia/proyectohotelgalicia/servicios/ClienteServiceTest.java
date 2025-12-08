package com.hotelgalicia.proyectohotelgalicia.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository cRep;

    @Mock
    private UsuarioRepository uRep;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private ClienteServiceImpl cServ;

    private Cliente cliente;
    private ClienteDTO dto;
    // private Usuario admin;
    private Usuario user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cliente = Cliente.builder()
                .id(1L)
                .correo("test@example.com")
                .contraseña("encodedpass")
                .rol(Roles.USER)
                .estado(true)
                .nombre("Test")
                .apellido("User")
                .telefono("600000000")
                .build();

        dto = new ClienteDTO();
        dto.setCorreo("new@example.com");
        dto.setContraseña("Clave123");
        dto.setNombre("Nuevo");
        dto.setApellido("Usuario");
        dto.setTelefono("699999999");

        // admin =
        // Usuario.builder().id(99L).correo("admin@example.com").rol(Roles.ADMIN).build();
        user = Usuario.builder().id(1L).correo("test@example.com").rol(Roles.USER).build();
    }

    // -------------------------------
    // Métodos de lectura
    // -------------------------------
    @Test
    void listAll_OK() {
        when(cRep.findAll()).thenReturn(List.of(cliente));
        var result = cServ.listAll();
        assertEquals(1, result.size());
        verify(cRep, times(1)).findAll();
    }

    @Test
    void getById_OK() {
        when(cRep.findById(1L)).thenReturn(Optional.of(cliente));
        Cliente found = cServ.getById(1L);
        assertEquals("test@example.com", found.getCorreo());
    }

    @Test
    void getById_NotFound() {
        when(cRep.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cServ.getById(99L));
    }

    @Test
    void getByCorreo_OK() {
        when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
        Cliente found = cServ.getByCorreo("test@example.com");
        assertEquals("test@example.com", found.getCorreo());
    }

    @Test
    void getByCorreo_NotFound() {
        when(cRep.findByCorreoIgnoreCase("nope@example.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cServ.getByCorreo("nope@example.com"));
    }

    // -------------------------------
    // Agregar
    // -------------------------------
    @Test
    void agregar_OK() {
        when(uRep.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn("encodedpass");
        when(cRep.save(any(Cliente.class))).thenReturn(cliente);

        Cliente added = cServ.agregar(dto);
        assertNotNull(added);
        verify(cRep).save(any(Cliente.class));
    }

    @Test
    void agregar_CorreoEnUso() {
        when(uRep.findByCorreo(dto.getCorreo())).thenReturn(Optional.of(cliente));
        assertThrows(RuntimeException.class, () -> cServ.agregar(dto));
        verify(cRep, never()).save(any());
    }

    @Test
    void agregar_ErrorEnSave() {
        when(uRep.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn("encodedpass");
        when(cRep.save(any())).thenThrow(new RuntimeException("Error en BD"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> cServ.agregar(dto));
        assertTrue(ex.getMessage().contains("Error inesperado al agregar"));
    }

    // -------------------------------
    // Modificar
    // -------------------------------
    @Test
    void modificar_OK() {
        mockAuth("test@example.com", user);
        when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
        when(uRep.findByCorreo(dto.getCorreo().trim().toLowerCase())).thenReturn(Optional.empty());
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(cRep.save(any(Cliente.class))).thenReturn(cliente);

        Cliente modificado = cServ.modificar(dto);
        assertNotNull(modificado);
        verify(cRep, times(1)).save(any());
    }

    @Test
    void modificar_ContraseñaIncorrecta() {
        mockAuth("test@example.com", user);
        when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
        when(uRep.findByCorreo(dto.getCorreo().trim().toLowerCase())).thenReturn(Optional.empty());
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> cServ.modificar(dto));
    }

    @Test
    void modificar_ErrorEnSave() {
        mockAuth("test@example.com", user);
        when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
        when(uRep.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(cRep.save(any())).thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> cServ.agregar(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("error inesperado"));
    }

    // -------------------------------
    // Cambiar contraseña
    // -------------------------------
    // @Test
    // void cambiarContraseña_OK() {
    //     mockAuth("test@example.com", user);
    //     when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
    //     when(encoder.matches(eq("oldpass"), anyString())).thenReturn(true);
    //     when(encoder.matches(eq("newpass"), anyString())).thenReturn(false);
    //     when(encoder.encode("newpass")).thenReturn("encodedNew");

    //     ClaveDTO clave = new ClaveDTO("oldpass", "newpass");
    //     Boolean result = cServ.cambiarContraseña(clave);
    //     assertTrue(result);
    //     verify(cRep).save(any());
    // }

    // @Test
    // void cambiarContraseña_Incorrecta() {
    //     mockAuth("test@example.com", user);
    //     when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
    //     when(encoder.matches(eq("badpass"), anyString())).thenReturn(false);

    //     ClaveDTO clave = new ClaveDTO("badpass", "newpass");
    //     assertThrows(BadCredentialsException.class, () -> cServ.cambiarContraseña(clave));
    // }

    // @Test
    // void cambiarContraseña_MismaQueActual() {
    //     mockAuth("test@example.com", user);
    //     when(cRep.findByCorreoIgnoreCase("test@example.com")).thenReturn(Optional.of(cliente));
    //     when(encoder.matches(eq("oldpass"), anyString())).thenReturn(true);

    //     ClaveDTO clave = new ClaveDTO("oldpass", "oldpass");
    //     assertThrows(BadCredentialsException.class, () -> cServ.cambiarContraseña(clave));
    // }

    // -------------------------------
    // Helper
    // -------------------------------
    private void mockAuth(String correo, Usuario usuario) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(correo);

        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        when(uRep.findByCorreo(correo)).thenReturn(Optional.of(usuario));
    }
}
