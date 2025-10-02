package com.hotelgalicia.proyectohotelgalicia.configs;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;

@Configuration
public class DatosIniciales {
    // CommandLineRunner comentado mientras la base de datos esté en modo validate
    // @Autowired
    // private PasswordEncoder encoder;

    @Bean
    CommandLineRunner initData(
            ClienteRepository cRep,
            EmpresaRepository eRep) {
        return args -> {

            List<Cliente> clientes = List.of(
                    Cliente.builder()
                            .correo("carlos.perez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Carlos").apellido("Pérez").telefono("612345678").build(),
                    Cliente.builder()
                            .correo("maria.lopez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("María").apellido("López").telefono("622345678").build(),
                    Cliente.builder()
                            .correo("juan.gonzalez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Juan").apellido("González").telefono("632345678").build(),
                    Cliente.builder()
                            .correo("ana.martinez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Ana").apellido("Martínez").telefono("642345678").build(),
                    Cliente.builder()
                            .correo("pedro.ramirez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Pedro").apellido("Ramírez").telefono("652345678").build(),
                    Cliente.builder()
                            .correo("laura.fernandez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Laura").apellido("Fernández").telefono("662345678").build(),
                    Cliente.builder()
                            .correo("david.sanchez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("David").apellido("Sánchez").telefono("672345678").build(),
                    Cliente.builder()
                            .correo("sofia.castro@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Sofía").apellido("Castro").telefono("682345678").build(),
                    Cliente.builder()
                            .correo("ricardo.herrera@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Ricardo").apellido("Herrera").telefono("692345678").build(),
                    Cliente.builder()
                            .correo("elena.mendez@example.com").contraseña("12345").rol(Roles.USER).estado(true)
                            .nombre("Elena").apellido("Méndez").telefono("602345678").build());

            cRep.saveAll(clientes);

            List<Empresa> empresas = List.of(
                    Empresa.builder()
                            .correo("contacto@hotelgalicia.com").contraseña("12345").rol(Roles.CORPORATION).estado(true)
                            .razonSocial("Hoteles Galicia S.L.").cif("B12345678").build(),
                    Empresa.builder()
                            .correo("info@viajesatlantico.com").contraseña("12345").rol(Roles.CORPORATION).estado(true)
                            .razonSocial("Viajes Atlántico S.A.").cif("B23456789").build(),
                    Empresa.builder()
                            .correo("contacto@riasresort.com").contraseña("12345").rol(Roles.CORPORATION).estado(true)
                            .razonSocial("Rías Baixas Resort S.L.").cif("B34567890").build(),
                    Empresa.builder()
                            .correo("reservas@turismoverde.com").contraseña("12345").rol(Roles.CORPORATION).estado(true)
                            .razonSocial("Turismo Verde S.A.").cif("B45678901").build(),
                    Empresa.builder()
                            .correo("info@galicialuxury.com").contraseña("12345").rol(Roles.CORPORATION).estado(true)
                            .razonSocial("Galicia Luxury Hotels S.L.").cif("B56789012").build());

            eRep.saveAll(empresas);
        };
    }
}