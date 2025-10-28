// package com.hotelgalicia.proyectohotelgalicia.servicios;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
// import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
// import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
// import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
// import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

// @SpringBootTest
// @TestInstance(Lifecycle.PER_CLASS)
// public class ClienteServiceTest {
//     private static final Logger log = LoggerFactory.getLogger(ClienteServiceTest.class);
//     @Autowired
//     private PasswordEncoder encoder;
    
//     @InjectMocks
//     private ClienteServiceImpl cServ;

//     @Mock
//     private ClienteRepository cRep;
//     @Mock
//     private UsuarioRepository uRep;
//     private List<Cliente> mockList;
//     private ClienteDTO clientesinid;
//     private Cliente clienteconid;

//     private Cliente base;
//     private ClienteDTO dto;
//     private Cliente modificado;

//     @BeforeAll
//     public void init() {
//         mockList = List.of(
//                 Cliente.builder().id(1l)
//                         .correo("carlos.perez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Carlos").apellido("Pérez").telefono("612345678").build(),
//                 Cliente.builder().id(2l)
//                         .correo("maria.lopez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("María").apellido("López").telefono("622345678").build(),
//                 Cliente.builder().id(3l)
//                         .correo("juan.gonzalez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Juan").apellido("González").telefono("632345678").build(),
//                 Cliente.builder().id(4l)
//                         .correo("ana.martinez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Ana").apellido("Martínez").telefono("642345678").build(),
//                 Cliente.builder().id(5l)
//                         .correo("pedro.ramirez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Pedro").apellido("Ramírez").telefono("652345678").build(),
//                 Cliente.builder().id(6l)
//                         .correo("laura.fernandez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Laura").apellido("Fernández").telefono("662345678").build(),
//                 Cliente.builder().id(7l)
//                         .correo("david.sanchez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("David").apellido("Sánchez").telefono("672345678").build(),
//                 Cliente.builder().id(8l)
//                         .correo("sofia.castro@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Sofía").apellido("Castro").telefono("682345678").build(),
//                 Cliente.builder().id(9l)
//                         .correo("elena.mendez@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                         .nombre("Elena").apellido("Méndez").telefono("602345678").build());
//         clienteconid = Cliente.builder().id(10l)
//                 .correo("ricardo.herrera@example.com").contraseña(encoder.encode("Clave12345")).rol(Roles.USER).estado(true)
//                 .nombre("Ricardo").apellido("Herrera").telefono("692345678").build();
//         clientesinid = new ClienteDTO();
//         clientesinid.setCorreo("ricardo.herrera@example.com");
//         clientesinid.setContraseña(encoder.encode("Clave12345"));
//         clientesinid.setNombre("Ricardo");
//         clientesinid.setApellido("Herrera");
//         clientesinid.setTelefono("692345678");

//         base = mockList.get(0);
//         dto = new ClienteDTO();
//         // dto.setId(base.getId());
//         dto.setCorreo("carlos.nuevo@example.com");
//         dto.setContraseña(base.getContraseña());
//         dto.setNombre("Carlos Nuevo");
//         dto.setApellido("Perez Nuevo");
//         dto.setTelefono("699999999");

//         modificado = Cliente.builder()
//                 .id(base.getId())
//                 .correo(dto.getCorreo())
//                 .contraseña(base.getContraseña())
//                 .rol(base.getRol())
//                 .estado(base.getEstado())
//                 .nombre(dto.getNombre())
//                 .apellido(dto.getApellido())
//                 .telefono(dto.getTelefono())
//                 .build();
//     }

//     @Test
//     public void listAllTest() {
//         when(cRep.findAll()).thenReturn(mockList);
//         List<Cliente> clist = cServ.listAll();
//         assertEquals(9, clist.size());
//         assertEquals("maria.lopez@example.com", clist.get(1).getCorreo());
//         log.info("Clientes {}", clist);
//         verify(cRep, times(1)).findAll();
//     }

//     @Test
//     public void agregarOk() {
//         when(uRep.findByCorreo(clientesinid.getCorreo())).thenReturn(Optional.empty());

//         when(cRep.save(any(Cliente.class))).thenReturn(clienteconid);

//         Cliente agregado = cServ.agregar(clientesinid);

//         assertEquals(10L, agregado.getId());
//         assertEquals("ricardo.herrera@example.com", agregado.getCorreo());
//         assertEquals("Ricardo", agregado.getNombre());
//         assertEquals("Herrera", agregado.getApellido());
//         verify(cRep, times(1)).save(any(Cliente.class));
//         verify(uRep, times(1)).findByCorreo(clientesinid.getCorreo());
//     }

//     @Test
//     public void agregarFail1() {
//         when(uRep.findByCorreo(clientesinid.getCorreo())).thenReturn(Optional.of(clienteconid));

//         RuntimeException ex = assertThrows(RuntimeException.class,
//                 () -> cServ.agregar(clientesinid));

//         assertEquals("El correo ingresado ya está en uso.", ex.getMessage());
//         verify(uRep, times(1)).findByCorreo(clientesinid.getCorreo());
//         verify(cRep, never()).save(any());
//     }

//     @Test
//     public void agregarFail2() {
//         when(uRep.findByCorreo(clientesinid.getCorreo()))
//                 .thenReturn(Optional.empty());

//         when(cRep.save(any(Cliente.class)))
//                 .thenThrow(new RuntimeException("Error en BD"));

//         RuntimeException ex = assertThrows(RuntimeException.class,
//                 () -> cServ.agregar(clientesinid));
//         log.info("Mensaje de la excepción: {}", ex.getMessage());
//         assertTrue(ex.getMessage().contains("Error inesperado al agregar al usuario "));
//         verify(cRep, times(1)).save(any(Cliente.class));
//     }

// //     @Test
// //     void modificar_OK() {
// //         when(cRep.findById(dto.getId())).thenReturn(Optional.of(base));
// //         when(uRep.findByCorreo(dto.getCorreo().trim().toLowerCase())).thenReturn(Optional.empty());
// //         when(cRep.save(any(Cliente.class))).thenReturn(modificado);

// //         Cliente resultado = cServ.modificar(dto);

// //         assertNotNull(resultado);
// //         assertEquals("carlos.nuevo@example.com", resultado.getCorreo());
// //         verify(cRep, times(1)).save(any(Cliente.class));
// //     }

// //     @Test
// //     void modificar_FallaPorNoEncontrado() {
// //         when(cRep.findById(dto.getId())).thenReturn(Optional.empty());
// //         RuntimeException ex = assertThrows(RuntimeException.class, () -> cServ.modificar(dto));
// //         assertTrue(ex.getMessage().contains("No se pudieron recuperar los datos del usuario"));
// //     }

// //     @Test
// //     void modificar_FallaPorCorreoEnUso() {
// //         Cliente otro = Cliente.builder().id(2L).correo("carlos.nuevo@example.com").build();
// //         when(cRep.findById(dto.getId())).thenReturn(Optional.of(base));
// //         when(uRep.findByCorreo(dto.getCorreo().trim().toLowerCase())).thenReturn(Optional.of(otro));
// //         RuntimeException ex = assertThrows(RuntimeException.class, () -> cServ.modificar(dto));
// //         assertTrue(ex.getMessage().contains("ya está en uso"));
// //     }

// //     @Test
// //     void modificar_FallaPorContraseñaIncorrecta() {
// //         dto.setContraseña("99999");
// //         when(cRep.findById(dto.getId())).thenReturn(Optional.of(base));
// //         when(uRep.findByCorreo(dto.getCorreo().trim().toLowerCase())).thenReturn(Optional.empty());
// //         RuntimeException ex = assertThrows(RuntimeException.class, () -> cServ.modificar(dto));
// //         assertTrue(ex.getMessage().contains("Contraseña incorrecta"));
// //     }

// //     @Test
// //     void modificar_FallaPorErrorEnSave() {
// //         when(cRep.findById(dto.getId())).thenReturn(Optional.of(base));
// //         when(uRep.findByCorreo(dto.getCorreo().trim().toLowerCase())).thenReturn(Optional.empty());
// //         when(cRep.save(any(Cliente.class))).thenThrow(new RuntimeException("Error en BD"));
// //         RuntimeException ex = assertThrows(RuntimeException.class, () -> cServ.modificar(dto));
// //         assertTrue(ex.getMessage().contains("Error al modificar datos del usuario"));
// //     }
// }
