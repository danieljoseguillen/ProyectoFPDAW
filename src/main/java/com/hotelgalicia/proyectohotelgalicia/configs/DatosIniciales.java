package com.hotelgalicia.proyectohotelgalicia.configs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.DetalleReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;

@Configuration
public class DatosIniciales {
        // CommandLineRunner comentado mientras la base de datos esté en modo validate
        @Autowired
        private PasswordEncoder encoder;

        @Bean
        CommandLineRunner initData(
                        UsuarioRepository uRep, ClienteRepository cRep,
                        EmpresaRepository eRep, HotelRepository hoRep,
                        HabitacionRepository haRep, ReservaRepository reRep, DetalleReservaRepository drRep,
                        ClienteService cServ, EmpresaService eServ, HabitacionService haServ, HotelService hoServ,
                        ReservaService reServ) {
                return args -> {
                        Usuario admin = Usuario.builder()
                                        .correo("admin@example.com")
                                        .contraseña(encoder.encode(encoder.encode("Clave12345")))
                                        .rol(Roles.ADMIN).estado(true).build();
                        // Clientes
                        List<Cliente> clientes = List.of(
                                        Cliente.builder()
                                                        .correo("carlos.perez@example.com")
                                                        .contraseña(encoder.encode(encoder.encode("Clave12345")))
                                                        .rol(Roles.USER).estado(true)
                                                        .nombre("Carlos").apellido("Pérez").telefono("612345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("maria.lopez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("María").apellido("López").telefono("622345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("juan.gonzalez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Juan").apellido("González").telefono("632345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("ana.martinez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Ana").apellido("Martínez").telefono("642345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("pedro.ramirez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Pedro").apellido("Ramírez").telefono("652345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("laura.fernandez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Laura").apellido("Fernández").telefono("662345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("david.sanchez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("David").apellido("Sánchez").telefono("672345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("sofia.castro@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Sofía").apellido("Castro").telefono("682345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("ricardo.herrera@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Ricardo").apellido("Herrera").telefono("692345678")
                                                        .build(),
                                        Cliente.builder()
                                                        .correo("elena.mendez@example.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.USER)
                                                        .estado(true)
                                                        .nombre("Elena").apellido("Méndez").telefono("602345678")
                                                        .build());

                        // Empresas
                        List<Empresa> empresas = List.of(
                                        Empresa.builder()
                                                        .correo("contacto@hotelgalicia.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.CORPORATION)
                                                        .estado(true)
                                                        .razonSocial("Hoteles Galicia S.L.").cif("B12345678").build(),
                                        Empresa.builder()
                                                        .correo("info@viajesatlantico.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.CORPORATION)
                                                        .estado(true)
                                                        .razonSocial("Viajes Atlántico S.A.").cif("B23456789").build(),
                                        Empresa.builder()
                                                        .correo("contacto@riasresort.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.CORPORATION)
                                                        .estado(true)
                                                        .razonSocial("Rías Baixas Resort S.L.").cif("B34567890")
                                                        .build(),
                                        Empresa.builder()
                                                        .correo("reservas@turismoverde.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.CORPORATION)
                                                        .estado(true)
                                                        .razonSocial("Turismo Verde S.A.").cif("B45678901").build(),
                                        Empresa.builder()
                                                        .correo("info@galicialuxury.com")
                                                        .contraseña(encoder.encode("Clave12345")).rol(Roles.CORPORATION)
                                                        .estado(true)
                                                        .razonSocial("Galicia Luxury Hotels S.L.").cif("B56789012")
                                                        .build());

                        // Hoteles
                        List<Hotel> hoteles = List.of(
                                        Hotel.builder()
                                                        .nombre("Hotel Rías Altas")
                                                        .descripcion("Ubicado frente al mar en A Coruña, ofrece habitaciones modernas con vistas al Atlántico y acceso directo a la playa de Bastiagueiro.")
                                                        .municipio(Municipios.A_CORUÑA)
                                                        .direccion("Av. Ernesto Che Guevara 81, Oleiros, A Coruña")
                                                        .telefono("981123456")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.3)
                                                        .empresa(empresas.get(0))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Gran Hotel Santiago")
                                                        .descripcion("Moderno hotel a pocos minutos de la Catedral de Santiago, con amplias habitaciones y un centro de convenciones de primer nivel.")
                                                        .municipio(Municipios.SANTIAGO_DE_COMPOSTELA)
                                                        .direccion("Av. Maestro Mateo 27, Santiago de Compostela")
                                                        .telefono("981654321")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.6)
                                                        .empresa(empresas.get(1))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Rías Baixas Resort")
                                                        .descripcion("Complejo turístico con spa, piscina exterior e impresionantes vistas a la ría de Arousa. Ideal para escapadas románticas.")
                                                        .municipio(Municipios.VILAGARCÍA_DE_AROUSA)
                                                        .direccion("Carretera de Sanxenxo Km 2, Vilagarcía de Arousa")
                                                        .telefono("986111222")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.8)
                                                        .empresa(empresas.get(2))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel Rural O Bosque Encantado")
                                                        .descripcion("Alojamiento rural en la Ribeira Sacra, rodeado de naturaleza y tranquilidad. Cocina gallega tradicional y rutas de senderismo.")
                                                        .municipio(Municipios.MONFORTE_DE_LEMOS)
                                                        .direccion("Lugar de Seoane, s/n, Monforte de Lemos")
                                                        .telefono("982987654")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.5)
                                                        .empresa(empresas.get(3))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Galicia Luxury Suites")
                                                        .descripcion("Hotel de lujo en Vigo con suites panorámicas, gimnasio, piscina climatizada y restaurante gourmet.")
                                                        .municipio(Municipios.VIGO)
                                                        .direccion("Rúa de García Barbón 45, Vigo")
                                                        .telefono("986555999")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.9)
                                                        .empresa(empresas.get(4))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel Costa Verde")
                                                        .descripcion("Encantador hotel costero en Fisterra con habitaciones con vistas al océano y desayunos caseros con productos locales.")
                                                        .municipio(Municipios.FISTERRA)
                                                        .direccion("Rúa Real 12, Fisterra")
                                                        .telefono("981765432")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.4)
                                                        .empresa(empresas.get(0))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Pazo do Río Boutique")
                                                        .descripcion("Antiguo pazo gallego restaurado convertido en hotel boutique, rodeado de jardines y con piscina exterior.")
                                                        .municipio(Municipios.A_CORUÑA)
                                                        .direccion("Rúa do Pazo, 3, Oleiros, A Coruña")
                                                        .telefono("981998877")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.7)
                                                        .empresa(empresas.get(0))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel Atlántico Norte")
                                                        .descripcion("Hotel moderno y funcional en Ferrol con restaurante, gimnasio y amplias salas para eventos.")
                                                        .municipio(Municipios.FERROL)
                                                        .direccion("Av. Esteiro 155, Ferrol")
                                                        .telefono("981445566")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.2)
                                                        .empresa(empresas.get(1))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Monte do Silencio")
                                                        .descripcion("Pequeño hotel rural ecológico en Lugo con vistas a los montes gallegos. Ideal para desconectar.")
                                                        .municipio(Municipios.LUGO)
                                                        .direccion("Lugar de Vilariño, s/n, Lugo")
                                                        .telefono("982334455")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.5)
                                                        .empresa(empresas.get(3))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel do Mar")
                                                        .descripcion("Hotel familiar frente al puerto de Sanxenxo, con habitaciones luminosas y restaurante especializado en mariscos.")
                                                        .municipio(Municipios.SANXENXO)
                                                        .direccion("Rúa do Porto 15, Sanxenxo")
                                                        .telefono("986222333")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.4)
                                                        .empresa(empresas.get(2))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Camiño Real Inn")
                                                        .descripcion("Hotel tradicional ubicado en el Camino de Santiago, con alojamiento para peregrinos y viajeros. Ambiente acogedor y buena comida.")
                                                        .municipio(Municipios.ARZUA)
                                                        .direccion("Rúa do Camiño, 7, Arzúa")
                                                        .telefono("981224466")
                                                        .imagen("")
                                                        .estado(true)
                                                        .puntaje(4.1)
                                                        .empresa(empresas.get(1))
                                                        .build());

                        // Habitaciones
                        List<Habitacion> habitaciones = List.of(
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble con Vista al Mar")
                                                        .descripcion("Amplia habitación con balcón frente al Atlántico, equipada con cama doble, baño privado y televisión de pantalla plana.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(95.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(0))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Familiar")
                                                        .descripcion("Suite con dos dormitorios y sala de estar, ideal para familias. Vista lateral al mar y desayuno incluido.")
                                                        .cantidad(5)
                                                        .capacidad(4)
                                                        .precio(160.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(0))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Individual Económica")
                                                        .descripcion("Habitación cómoda y funcional para una persona, con escritorio, Wi-Fi gratuito y baño completo.")
                                                        .cantidad(6)
                                                        .capacidad(1)
                                                        .precio(65.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(0))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Moderna habitación con dos camas individuales o cama doble, climatización y vistas a la ciudad.")
                                                        .cantidad(12)
                                                        .capacidad(2)
                                                        .precio(90.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Ejecutiva")
                                                        .descripcion("Suite con zona de estar, escritorio y baño con bañera de hidromasaje. Perfecta para estancias de trabajo.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(150.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Triple Deluxe")
                                                        .descripcion("Habitación amplia con tres camas individuales, minibar y desayuno buffet incluido.")
                                                        .cantidad(6)
                                                        .capacidad(3)
                                                        .precio(120.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Presidencial")
                                                        .descripcion("Suite de lujo con salón privado, jacuzzi y terraza panorámica con vistas a la Catedral.")
                                                        .cantidad(2)
                                                        .capacidad(2)
                                                        .precio(280.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Habitación moderna con vistas al jardín, cama doble, aire acondicionado y Wi-Fi gratuito.")
                                                        .cantidad(15)
                                                        .capacidad(2)
                                                        .precio(85.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Superior con Terraza")
                                                        .descripcion("Habitación con terraza privada y vistas a la ría de Arousa. Incluye acceso al spa.")
                                                        .cantidad(8)
                                                        .capacidad(2)
                                                        .precio(130.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Romántica")
                                                        .descripcion("Suite con jacuzzi, cama king-size y decoración especial. Ideal para parejas.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(190.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Bungalow Familiar")
                                                        .descripcion("Alojamiento independiente con dos dormitorios, cocina equipada y acceso a piscina.")
                                                        .cantidad(5)
                                                        .capacidad(5)
                                                        .precio(240.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Rural")
                                                        .descripcion("Habitación acogedora con decoración rústica, vigas de madera y vistas al bosque.")
                                                        .cantidad(6)
                                                        .capacidad(2)
                                                        .precio(75.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(3))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Ribeira Sacra")
                                                        .descripcion("Suite con jacuzzi, chimenea y terraza privada con vistas al valle. Ideal para escapadas románticas.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(150.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(3))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Rural")
                                                        .descripcion("Espaciosa habitación con dos dormitorios conectados, ideal para familias.")
                                                        .cantidad(4)
                                                        .capacidad(4)
                                                        .precio(110.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(3))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Suite Deluxe")
                                                        .descripcion("Suite de lujo con cama king-size, vistas a la ría de Vigo y baño de mármol.")
                                                        .cantidad(8)
                                                        .capacidad(2)
                                                        .precio(220.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Panorámica")
                                                        .descripcion("Suite de esquina con ventanales de suelo a techo, jacuzzi y minibar premium.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(300.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Premium")
                                                        .descripcion("Elegante habitación con cama queen-size, ducha efecto lluvia y acceso al gimnasio.")
                                                        .cantidad(12)
                                                        .capacidad(2)
                                                        .precio(160.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Presidencial Vigo")
                                                        .descripcion("Suite exclusiva con terraza privada, comedor y servicio de mayordomo 24h.")
                                                        .cantidad(2)
                                                        .capacidad(2)
                                                        .precio(420.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble con Vista al Mar")
                                                        .descripcion("Habitación luminosa con balcón al océano, cama doble y desayuno incluido.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(90.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(5))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite con Terraza Panorámica")
                                                        .descripcion("Suite con amplia terraza privada frente al mar y jacuzzi exterior.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(180.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(5))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Vista Mar")
                                                        .descripcion("Habitación con dos camas dobles, vistas al mar y cocina americana.")
                                                        .cantidad(5)
                                                        .capacidad(4)
                                                        .precio(130.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(5))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Clásica")
                                                        .descripcion("Decoración elegante con mobiliario antiguo restaurado, cama doble y baño con ducha de piedra.")
                                                        .cantidad(8)
                                                        .capacidad(2)
                                                        .precio(115.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(6))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite del Pazo")
                                                        .descripcion("Suite en la antigua torre del pazo con vistas a los jardines, bañera de hidromasaje y chimenea.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(190.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(6))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Jardín")
                                                        .descripcion("Amplia habitación con acceso directo al jardín y dos camas dobles.")
                                                        .cantidad(4)
                                                        .capacidad(4)
                                                        .precio(140.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(6))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Individual Confort")
                                                        .descripcion("Ideal para estancias de trabajo, con escritorio, wifi y cama individual.")
                                                        .cantidad(6)
                                                        .capacidad(1)
                                                        .precio(70.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(7))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Habitación funcional con cama doble o dos individuales y baño privado.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(95.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(7))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Ejecutiva")
                                                        .descripcion("Amplia habitación con zona de trabajo, minibar y acceso gratuito al gimnasio.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(135.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(7))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Ecológica Doble")
                                                        .descripcion("Decorada con materiales sostenibles, incluye desayuno ecológico.")
                                                        .cantidad(6)
                                                        .capacidad(2)
                                                        .precio(85.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(8))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Cabaña de Madera")
                                                        .descripcion("Pequeña cabaña independiente en el bosque, ideal para parejas.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(120.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(8))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Rural")
                                                        .descripcion("Alojamiento para familias con dos habitaciones y vistas al valle.")
                                                        .cantidad(3)
                                                        .capacidad(4)
                                                        .precio(150.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(8))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Habitación cómoda con cama doble, baño privado y desayuno incluido.")
                                                        .cantidad(12)
                                                        .capacidad(2)
                                                        .precio(100.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(9))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Triple Familiar")
                                                        .descripcion("Habitación con tres camas individuales y balcón con vistas al puerto.")
                                                        .cantidad(6)
                                                        .capacidad(3)
                                                        .precio(135.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(9))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Frente al Mar")
                                                        .descripcion("Suite con vistas directas al océano, bañera de hidromasaje y minibar.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(210.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(9))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Individual Peregrino")
                                                        .descripcion("Sencilla pero cómoda, con cama individual y baño compartido.")
                                                        .cantidad(8)
                                                        .capacidad(1)
                                                        .precio(45.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(10))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Tradicional")
                                                        .descripcion("Habitación rústica con mobiliario clásico y baño privado.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(70.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(10))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Peregrinos")
                                                        .descripcion("Habitación con literas, ideal para grupos o familias en el Camino.")
                                                        .cantidad(4)
                                                        .capacidad(4)
                                                        .precio(90.0)
                                                        .imagen("")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(10))
                                                        .build());
                        uRep.save(admin);
                        cRep.saveAll(clientes);
                        eRep.saveAll(empresas);
                        hoRep.saveAll(hoteles);
                        haRep.saveAll(habitaciones);

                        ReservaDTO reservaDTO = new ReservaDTO();
                        reservaDTO.setFechaInicio(LocalDate.now().plusDays(1));
                        reservaDTO.setFechaFin(LocalDate.now().plusDays(3));
                        reservaDTO.setPersonas(2);

                        // Detalles de la reserva
                        DetalleReservaDTO det1 = new DetalleReservaDTO(1L, 1); // Habitacion ID 1, cantidad 1
                        DetalleReservaDTO det2 = new DetalleReservaDTO(2L, 1); // Habitacion ID 2, cantidad 1
                        DetalleReservaDTO det3 = new DetalleReservaDTO(3L, 1); // Habitacion ID 3, cantidad 1
                        reservaDTO.setHabitaciones(List.of(det1, det2));
                        System.out.println(reservaDTO.getHabitaciones());
                        reServ.agregar(reservaDTO, 1L);
                        reservaDTO.setPersonas(3);
                        reservaDTO.setHabitaciones(List.of(det1, det2, det3));
                        System.out.println(reservaDTO.getHabitaciones());
                        reServ.modificar(reservaDTO, 1L);
                };
        }
}