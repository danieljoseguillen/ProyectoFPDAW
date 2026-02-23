package com.hotelgalicia.proyectohotelgalicia.configs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ValoracionRepository;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;

        // CommandLineRunner comentado mientras la base de datos esté en modo validate
@Configuration
public class DatosIniciales {

        @Autowired
        private PasswordEncoder encoder;

        @Bean
        CommandLineRunner initData(
                        UsuarioRepository uRep, ClienteRepository cRep,
                        EmpresaRepository eRep, HotelRepository hoRep,
                        HabitacionRepository haRep, ReservaRepository reRep, DetalleReservaRepository drRep,
                        ClienteService cServ, EmpresaService eServ, HabitacionService haServ, HotelService hoServ,
                        ReservaService reServ, ValoracionRepository vaRep, TransactionalExecutor txExec) {
                return args -> txExec.run(() -> {
                        Usuario admin = Usuario.builder()
                                        .correo("admin@example.com")
                                        .contraseña(encoder.encode("Clave12345"))
                                        .rol(Roles.ADMIN).estado(true).build();
                        // Clientes
                        List<Cliente> clientes = List.of(
                                        Cliente.builder()
                                                        .correo("carlos.perez@example.com")
                                                        .contraseña(encoder.encode("Clave12345"))
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
                                                        .descripcion(
                                                                        "Ubicado frente al mar en A Coruña, el Hotel Rías Altas ofrece una experiencia tranquila y costera. "
                                                                                        +
                                                                                        "Sus habitaciones están diseñadas para maximizar las vistas al Atlántico, con grandes ventanales y una decoración moderna pero acogedora.\n\n"
                                                                                        +
                                                                                        "Además, dispone de acceso directo a la playa de Bastiagueiro, lo que permite a los huéspedes disfrutar de paseos al amanecer o relajarse al sol con total comodidad. "
                                                                                        +
                                                                                        "El hotel cuenta con un bar donde se puede tomar un café, una bebida o un snack mientras se escucha el murmullo de las olas.\n\n"
                                                                                        +
                                                                                        "La cercanía a puntos de interés costeros y la serenidad del entorno lo convierten en una opción ideal para quienes buscan desconectar en la costa norte de Galicia.")
                                                        .municipio(Municipios.A_CORUÑA)
                                                        .direccion("Av. Ernesto Che Guevara 81, Oleiros, A Coruña")
                                                        .telefono("981123456")
                                                        .imagen("hotel1.jpg")
                                                        .estado(true)
                                                        .puntaje(7.5)
                                                        .empresa(empresas.get(0))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Gran Hotel Santiago")
                                                        .descripcion(
                                                                        "El Gran Hotel Santiago se encuentra a pocos minutos de la majestuosa Catedral de Santiago, en pleno centro histórico de la ciudad. "
                                                                                        +
                                                                                        "Sus amplias habitaciones están equipadas con mobiliario elegante, tejidos de alta calidad y ventanas que ofrecen vistas a las calles empedradas.\n\n"
                                                                                        +
                                                                                        "El hotel dispone de un centro de convenciones moderno y versátil, ideal tanto para viajes de turismo como para estadías de negocios. "
                                                                                        +
                                                                                        "Además, cuenta con zona de descanso, recepción abierta 24 h y un desayuno buffet variado que satisface a huéspedes de todo tipo.\n\n"
                                                                                        +
                                                                                        "Su localización privilegiada permite llegar caminando a los principales puntos turísticos y ofrece una experiencia cómoda tanto para peregrinos como para visitantes culturales.")
                                                        .municipio(Municipios.SANTIAGO_DE_COMPOSTELA)
                                                        .direccion("Av. Maestro Mateo 27, Santiago de Compostela")
                                                        .telefono("981654321")
                                                        .imagen("hotel0.jpg")
                                                        .estado(true)
                                                        .puntaje(8.5)
                                                        .empresa(empresas.get(1))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Rías Baixas Resort")
                                                        .descripcion(
                                                                        "Rías Baixas Resort es un complejo turístico con encanto junto a la ría de Arousa, ideal para escapadas románticas o para relajarse. "
                                                                                        +
                                                                                        "Sus instalaciones incluyen un spa, piscina al aire libre y jardines cuidadosamente diseñados para disfrutar de la naturaleza gallega.\n\n"
                                                                                        +
                                                                                        "Las habitaciones ofrecen un ambiente elegante y confortable, con terrazas que permiten contemplar la ría o los paisajes verdes que rodean el resort. "
                                                                                        +
                                                                                        "Además, el resort tiene un restaurante con platos tradicionales de la cocina gallega, donde se pueden degustar mariscos frescos y vinos locales.\n\n"
                                                                                        +
                                                                                        "Gracias a su ubicación, los huéspedes tienen fácil acceso tanto a pueblos costeros tradicionales como a actividades acuáticas, lo que convierte al resort en un destino perfecto para descansar y explorar.")
                                                        .municipio(Municipios.VILAGARCÍA_DE_AROUSA)
                                                        .direccion("Carretera de Sanxenxo Km 2, Vilagarcía de Arousa")
                                                        .telefono("986111222")
                                                        .imagen("hotel3.jpg")
                                                        .estado(true)
                                                        .puntaje(5.5)
                                                        .empresa(empresas.get(2))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel Rural O Bosque Encantado")
                                                        .descripcion(
                                                                        "Situado en la Ribeira Sacra, el Hotel Rural O Bosque Encantado es un refugio de paz en medio de bosques y colinas. "
                                                                                        +
                                                                                        "Sus habitaciones combinan la estética rústica con comodidades modernas, ofreciendo chimeneas, mobiliario de madera y tejidos naturales.\n\n"
                                                                                        +
                                                                                        "El entorno natural invita a explorar senderos, hacer rutas de montaña o simplemente relajarse en sus jardines contemplando el paisaje gallego. "
                                                                                        +
                                                                                        "El hotel también ofrece desayunos con productos locales, elaborados con ingredientes de la región, y un comedor acogedor donde compartir historias junto al fuego.\n\n"
                                                                                        +
                                                                                        "Para quienes buscan desconectarse, este hotel es ideal: su atmósfera calmada y su proximidad a miradores naturales permiten una escapada auténtica y revitalizadora.")
                                                        .municipio(Municipios.MONFORTE_DE_LEMOS)
                                                        .direccion("Lugar de Seoane, s/n, Monforte de Lemos")
                                                        .telefono("982987654")
                                                        .imagen("hotel4.jpg")
                                                        .estado(true)
                                                        .puntaje(7.5)
                                                        .empresa(empresas.get(3))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Galicia Luxury Suites")
                                                        .descripcion(
                                                                        "Galicia Luxury Suites es un hotel de alto nivel en Vigo, con suites panorámicas, gimnasio, piscina climatizada y un restaurante gourmet. "
                                                                                        +
                                                                                        "Cada suite está diseñada con elegancia, ventanas de cristal y vistas espectaculares sobre la ciudad y la ría, combinando lujo y comodidad.\n\n"
                                                                                        +
                                                                                        "Los huéspedes pueden disfrutar de servicios premium, como spa privado, servicio de mayordomo, opciones de cenas exclusivas y ocio personalizado. "
                                                                                        +
                                                                                        "Además, el hotel organiza actividades culturales y de bienestar para integrar la experiencia de lujo con la autenticidad gallega.\n\n"
                                                                                        +
                                                                                        "Con una ubicación privilegiada, es el lugar perfecto tanto para escapadas románticas como para viajes de ocio orientados al relax, todo con un toque sofisticado.")
                                                        .municipio(Municipios.VIGO)
                                                        .direccion("Rúa de García Barbón 45, Vigo")
                                                        .telefono("986555999")
                                                        .imagen("hotel5.jpg")
                                                        .estado(true)
                                                        .puntaje(8.6)
                                                        .empresa(empresas.get(4))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel Costa Verde")
                                                        .descripcion(
                                                                        "El Hotel Costa Verde se alza en Fisterra, con habitaciones con vistas al océano y una atmósfera que evoca la brisa marina. "
                                                                                        +
                                                                                        "Sus espacios exteriores están pensados para relajarse, con terrazas desde donde se puede contemplar el mar, senderos costeros y rincones con encanto.\n\n"
                                                                                        +
                                                                                        "El interior del hotel combina estilo costero y confort: las habitaciones tienen decoración náutica suave, amplia iluminación y detalles inspirados en los materiales locales. "
                                                                                        +
                                                                                        "En la zona común se puede disfrutar de desayunos con productos gallegos, y el bar es un lugar ideal para ver el atardecer mientras se escucha el oleaje.\n\n"
                                                                                        +
                                                                                        "Por sus vistas privilegiadas y su tranquilidad, este hotel es una opción perfecta para quienes buscan una estancia íntima y conectada con la naturaleza costera.")
                                                        .municipio(Municipios.FISTERRA)
                                                        .direccion("Rúa Real 12, Fisterra")
                                                        .telefono("981765432")
                                                        .imagen("hotel0.jpg")
                                                        .estado(true)
                                                        .puntaje(7.0)
                                                        .empresa(empresas.get(0))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Pazo do Río Boutique")
                                                        .descripcion(
                                                                        "Ubicado en un antiguo pazo gallego restaurado, el Pazo do Río Boutique ofrece un viaje en el tiempo con todas las comodidades modernas. "
                                                                                        +
                                                                                        "Sus habitaciones conservan elementos arquitectónicos nobles: vigas de madera, suelos de piedra y grandes ventanales con vistas a los jardines.\n\n"
                                                                                        +
                                                                                        "Los jardines del pazo son un remanso de paz, con estanques, fuentes y zonas de lectura al aire libre. Por la tarde, el salón invita a relajarse con una copa de vino local. "
                                                                                        +
                                                                                        "También hay rutas cercanas por senderos privados que permiten descubrir la belleza natural del entorno, desde bosques hasta pequeños miradores.\n\n"
                                                                                        +
                                                                                        "Ideal para escapadas románticas o para viajeros que desean experimentar la historia de Galicia desde un lugar con carácter y elegancia.")
                                                        .municipio(Municipios.A_CORUÑA)
                                                        .direccion("Rúa do Pazo, 3, Oleiros, A Coruña")
                                                        .telefono("981998877")
                                                        .imagen("hotel7.jpg")
                                                        .estado(true)
                                                        .puntaje(8.0)
                                                        .empresa(empresas.get(0))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel Atlántico Norte")
                                                        .descripcion(
                                                                        "El Hotel Atlántico Norte en Ferrol combina funcionalidad moderna con un entorno urbano y profesional. "
                                                                                        +
                                                                                        "Sus habitaciones son amplias, climatizadas y equipadas con escritorio, lo que las hace perfectas tanto para huéspedes de negocio como para quienes viajan por placer.\n\n"
                                                                                        +
                                                                                        "El hotel dispone de sala de reuniones, gimnasio y amplias zonas comunes para relajarse, trabajar o socializar. Además, su ubicación permite un rápido acceso a puntos estratégicos de la ciudad, tanto comerciales como turísticos.\n\n"
                                                                                        +
                                                                                        "Desde sus ventanas se puede contemplar la ciudad portuaria y sus alrededores, lo que aporta una experiencia genuina de Ferrol con la comodidad de un alojamiento moderno.")
                                                        .municipio(Municipios.FERROL)
                                                        .direccion("Av. Esteiro 155, Ferrol")
                                                        .telefono("981445566")
                                                        .imagen("hotel8.jpg")
                                                        .estado(true)
                                                        .puntaje(8.6)
                                                        .empresa(empresas.get(1))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Monte do Silencio")
                                                        .descripcion(
                                                                        "Monte do Silencio es un pequeño hotel rural ecológico situado en Lugo, ideal para desconectar en un entorno natural tranquilo. "
                                                                                        +
                                                                                        "Las habitaciones están decoradas con materiales sostenibles y ofrecen vistas a los bosques verdes y colinas suaves que rodean la propiedad.\n\n"
                                                                                        +
                                                                                        "El hotel promueve actividades de bienestar: spa natural, yoga al amanecer, paseos ecológicos y zonas de meditación, todas diseñadas para armonizar con el paisaje gallego. "
                                                                                        +
                                                                                        "Por la noche, el cielo se convierte en un manto estrellado, y los huéspedes pueden sentarse al exterior para contemplar el firmamento en total tranquilidad.\n\n"
                                                                                        +
                                                                                        "Además, el restaurante ofrece menús con ingredientes locales y de temporada, conectando la gastronomía con la filosofía de sostenibilidad y paz del lugar.")
                                                        .municipio(Municipios.LUGO)
                                                        .direccion("Lugar de Vilariño, s/n, Lugo")
                                                        .telefono("982334455")
                                                        .imagen("hotel9.png")
                                                        .estado(true)
                                                        .puntaje(7.0)
                                                        .empresa(empresas.get(3))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Hotel do Mar")
                                                        .descripcion(
                                                                        "El Hotel do Mar, frente al puerto de Sanxenxo, es un lugar familiar con encanto náutico. "
                                                                                        +
                                                                                        "Sus habitaciones están diseñadas para ofrecer luz y amplitud, con camas cómodas, ventanas amplias y vistas al mar o al puerto.\n\n"
                                                                                        +
                                                                                        "La gastronomía es protagonista en su restaurante: mariscos, pescado fresco y platos gallegos tradicionales que se pueden degustar con el sonido del agua de fondo. "
                                                                                        +
                                                                                        "Para relajarse, el hotel cuenta con una terraza donde disfrutar de una copa al atardecer o simplemente contemplar los barcos que pasan por el puerto.\n\n"
                                                                                        +
                                                                                        "Es un punto de partida perfecto para explorar la ría, hacer excursiones en barco o simplemente descansar con el vaivén del mar como banda sonora.")
                                                        .municipio(Municipios.SANXENXO)
                                                        .direccion("Rúa do Porto 15, Sanxenxo")
                                                        .telefono("986222333")
                                                        .imagen("hotel10.png")
                                                        .estado(true)
                                                        .puntaje(8.0)
                                                        .empresa(empresas.get(2))
                                                        .build(),

                                        Hotel.builder()
                                                        .nombre("Camiño Real Inn")
                                                        .descripcion(
                                                                        "El Camiño Real Inn es un hotel tradicional ubicado en el Camino de Santiago, ofreciendo un ambiente acogedor para peregrinos y viajeros. "
                                                                                        +
                                                                                        "Sus habitaciones son funcionales pero confortables, con detalles clásicos y ventanas que permiten descansar tras el largo camino.\n\n"
                                                                                        +
                                                                                        "El hotel cuenta con zonas comunes para compartir experiencias: salones, comedor, biblioteca y espacios para conversar con otros huéspedes. "
                                                                                        +
                                                                                        "Además, organiza desayunos tempranos para peregrinos y proporciona información turística, mapas y recomendaciones para continuar el viaje.\n\n"
                                                                                        +
                                                                                        "Su ubicación lo convierte en una parada estratégica: cerca de rutas históricas y puntos de interés, es ideal tanto para caminantes como para visitantes que quieren empaparse de tradición gallega.")
                                                        .municipio(Municipios.ARZUA)
                                                        .direccion("Rúa do Camiño, 7, Arzúa")
                                                        .telefono("981224466")
                                                        .imagen("hotel11.png")
                                                        .estado(true)
                                                        .puntaje(7.5)
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
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(0))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Familiar")
                                                        .descripcion("Suite con dos dormitorios y sala de estar, ideal para familias. Vista lateral al mar y desayuno incluido.")
                                                        .cantidad(5)
                                                        .capacidad(4)
                                                        .precio(160.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(0))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Individual Económica")
                                                        .descripcion("Habitación cómoda y funcional para una persona, con escritorio, Wi-Fi gratuito y baño completo.")
                                                        .cantidad(6)
                                                        .capacidad(1)
                                                        .precio(65.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(0))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Moderna habitación con dos camas individuales o cama doble, climatización y vistas a la ciudad.")
                                                        .cantidad(12)
                                                        .capacidad(2)
                                                        .precio(90.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Ejecutiva")
                                                        .descripcion("Suite con zona de estar, escritorio y baño con bañera de hidromasaje. Perfecta para estancias de trabajo.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(150.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Triple Deluxe")
                                                        .descripcion("Habitación amplia con tres camas individuales, minibar y desayuno buffet incluido.")
                                                        .cantidad(6)
                                                        .capacidad(3)
                                                        .precio(120.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Presidencial")
                                                        .descripcion("Suite de lujo con salón privado, jacuzzi y terraza panorámica con vistas a la Catedral.")
                                                        .cantidad(2)
                                                        .capacidad(2)
                                                        .precio(280.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(1))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Habitación moderna con vistas al jardín, cama doble, aire acondicionado y Wi-Fi gratuito.")
                                                        .cantidad(15)
                                                        .capacidad(2)
                                                        .precio(85.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Superior con Terraza")
                                                        .descripcion("Habitación con terraza privada y vistas a la ría de Arousa. Incluye acceso al spa.")
                                                        .cantidad(8)
                                                        .capacidad(2)
                                                        .precio(130.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Romántica")
                                                        .descripcion("Suite con jacuzzi, cama king-size y decoración especial. Ideal para parejas.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(190.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Bungalow Familiar")
                                                        .descripcion("Alojamiento independiente con dos dormitorios, cocina equipada y acceso a piscina.")
                                                        .cantidad(5)
                                                        .capacidad(5)
                                                        .precio(240.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(2))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Rural")
                                                        .descripcion("Habitación acogedora con decoración rústica, vigas de madera y vistas al bosque.")
                                                        .cantidad(6)
                                                        .capacidad(2)
                                                        .precio(75.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(3))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Ribeira Sacra")
                                                        .descripcion("Suite con jacuzzi, chimenea y terraza privada con vistas al valle. Ideal para escapadas románticas.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(150.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(3))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Rural")
                                                        .descripcion("Espaciosa habitación con dos dormitorios conectados, ideal para familias.")
                                                        .cantidad(4)
                                                        .capacidad(4)
                                                        .precio(110.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(3))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Suite Deluxe")
                                                        .descripcion("Suite de lujo con cama king-size, vistas a la ría de Vigo y baño de mármol.")
                                                        .cantidad(8)
                                                        .capacidad(2)
                                                        .precio(220.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Panorámica")
                                                        .descripcion("Suite de esquina con ventanales de suelo a techo, jacuzzi y minibar premium.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(300.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Premium")
                                                        .descripcion("Elegante habitación con cama queen-size, ducha efecto lluvia y acceso al gimnasio.")
                                                        .cantidad(12)
                                                        .capacidad(2)
                                                        .precio(160.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Presidencial Vigo")
                                                        .descripcion("Suite exclusiva con terraza privada, comedor y servicio de mayordomo 24h.")
                                                        .cantidad(2)
                                                        .capacidad(2)
                                                        .precio(420.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(4))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble con Vista al Mar")
                                                        .descripcion("Habitación luminosa con balcón al océano, cama doble y desayuno incluido.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(90.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(5))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite con Terraza Panorámica")
                                                        .descripcion("Suite con amplia terraza privada frente al mar y jacuzzi exterior.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(180.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(5))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Vista Mar")
                                                        .descripcion("Habitación con dos camas dobles, vistas al mar y cocina americana.")
                                                        .cantidad(5)
                                                        .capacidad(4)
                                                        .precio(130.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(5))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Clásica")
                                                        .descripcion("Decoración elegante con mobiliario antiguo restaurado, cama doble y baño con ducha de piedra.")
                                                        .cantidad(8)
                                                        .capacidad(2)
                                                        .precio(115.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(6))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite del Pazo")
                                                        .descripcion("Suite en la antigua torre del pazo con vistas a los jardines, bañera de hidromasaje y chimenea.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(190.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(6))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Jardín")
                                                        .descripcion("Amplia habitación con acceso directo al jardín y dos camas dobles.")
                                                        .cantidad(4)
                                                        .capacidad(4)
                                                        .precio(140.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(6))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Individual Confort")
                                                        .descripcion("Ideal para estancias de trabajo, con escritorio, wifi y cama individual.")
                                                        .cantidad(6)
                                                        .capacidad(1)
                                                        .precio(70.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(7))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Habitación funcional con cama doble o dos individuales y baño privado.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(95.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(7))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Ejecutiva")
                                                        .descripcion("Amplia habitación con zona de trabajo, minibar y acceso gratuito al gimnasio.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(135.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(7))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Ecológica Doble")
                                                        .descripcion("Decorada con materiales sostenibles, incluye desayuno ecológico.")
                                                        .cantidad(6)
                                                        .capacidad(2)
                                                        .precio(85.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(8))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Cabaña de Madera")
                                                        .descripcion("Pequeña cabaña independiente en el bosque, ideal para parejas.")
                                                        .cantidad(4)
                                                        .capacidad(2)
                                                        .precio(120.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(8))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Rural")
                                                        .descripcion("Alojamiento para familias con dos habitaciones y vistas al valle.")
                                                        .cantidad(3)
                                                        .capacidad(4)
                                                        .precio(150.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(8))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Estándar")
                                                        .descripcion("Habitación cómoda con cama doble, baño privado y desayuno incluido.")
                                                        .cantidad(12)
                                                        .capacidad(2)
                                                        .precio(100.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(9))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Triple Familiar")
                                                        .descripcion("Habitación con tres camas individuales y balcón con vistas al puerto.")
                                                        .cantidad(6)
                                                        .capacidad(3)
                                                        .precio(135.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(9))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Suite Frente al Mar")
                                                        .descripcion("Suite con vistas directas al océano, bañera de hidromasaje y minibar.")
                                                        .cantidad(3)
                                                        .capacidad(2)
                                                        .precio(210.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(9))
                                                        .build(),
                                        Habitacion.builder()
                                                        .nombre("Habitación Individual Peregrino")
                                                        .descripcion("Sencilla pero cómoda, con cama individual y baño compartido.")
                                                        .cantidad(8)
                                                        .capacidad(1)
                                                        .precio(45.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(10))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Doble Tradicional")
                                                        .descripcion("Habitación rústica con mobiliario clásico y baño privado.")
                                                        .cantidad(10)
                                                        .capacidad(2)
                                                        .precio(70.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(10))
                                                        .build(),

                                        Habitacion.builder()
                                                        .nombre("Habitación Familiar Peregrinos")
                                                        .descripcion("Habitación con literas, ideal para grupos o familias en el Camino.")
                                                        .cantidad(4)
                                                        .capacidad(4)
                                                        .precio(90.0)
                                                        .imagen("habitacion1.jpg")
                                                        .estado(EstadoHabitacion.DISPONIBLE)
                                                        .hotel(hoteles.get(10))
                                                        .build());
                        List<Valoracion> valoraciones = List.of(

                                        // --- Hotel 1: Hotel Rías Altas ---
                                        new Valoracion(hoteles.get(0), clientes.get(0),
                                                        LocalDate.now().minusDays(3), 8,
                                                        "Buena experiencia, repetiría."),
                                        new Valoracion(hoteles.get(0), clientes.get(1),
                                                        LocalDate.now().minusDays(10), 7,
                                                        "Bonitas vistas, pero algo ruidoso."),
                                        new Valoracion(hoteles.get(0), clientes.get(2),
                                                        LocalDate.now().minusMonths(1), 9,
                                                        "Excelente ubicación frente al mar."),
                                        new Valoracion(hoteles.get(0), clientes.get(3),
                                                        LocalDate.now().minusDays(20), 6, "Cumple, pero esperaba más."),
                                        new Valoracion(hoteles.get(0), clientes.get(4),
                                                        LocalDate.now().minusDays(35), 8,
                                                        "Habitaciones limpias y bien equipadas."),
                                        new Valoracion(hoteles.get(0), clientes.get(5),
                                                        LocalDate.now().minusDays(42), 7,
                                                        "Personal muy atencioso y amable."),
                                        new Valoracion(hoteles.get(0), clientes.get(6),
                                                        LocalDate.now().minusDays(15), 9,
                                                        "Acceso a playa excelente."),
                                        new Valoracion(hoteles.get(0), clientes.get(7),
                                                        LocalDate.now().minusMonths(3).minusDays(5), 7,
                                                        "Buen bar, ambiente tranquilo."),
                                        new Valoracion(hoteles.get(0), clientes.get(8),
                                                        LocalDate.now().minusDays(61), 8,
                                                        "Playas cercanas hermosas."),
                                        new Valoracion(hoteles.get(0), clientes.get(9),
                                                        LocalDate.now().minusDays(13), 7,
                                                        "Atardecer espectacular desde la playa."),
                                        // Media: 7.5

                                        // --- Hotel 2: Gran Hotel Santiago ---
                                        new Valoracion(hoteles.get(1), clientes.get(4),
                                                        LocalDate.now().minusDays(5), 9, "Muy moderno y bien ubicado."),
                                        new Valoracion(hoteles.get(1), clientes.get(5),
                                                        LocalDate.now().minusDays(12), 8,
                                                        "Todo correcto, buen servicio."),
                                        new Valoracion(hoteles.get(1), clientes.get(6),
                                                        LocalDate.now().minusMonths(2), 7, "Habitación cómoda."),
                                        new Valoracion(hoteles.get(1), clientes.get(7),
                                                        LocalDate.now().minusDays(1), 10,
                                                        "Perfecto para una escapada."),
                                        new Valoracion(hoteles.get(1), clientes.get(8),
                                                        LocalDate.now().minusDays(33), 9,
                                                        "Centro de convenciones impresionante."),
                                        new Valoracion(hoteles.get(1), clientes.get(9),
                                                        LocalDate.now().minusDays(48), 8,
                                                        "Desayuno buffet excelente."),
                                        new Valoracion(hoteles.get(1), clientes.get(0),
                                                        LocalDate.now().minusDays(21), 10,
                                                        "Ubicación junto a la Catedral perfecta."),
                                        new Valoracion(hoteles.get(1), clientes.get(1),
                                                        LocalDate.now().minusMonths(4).plusDays(10), 8,
                                                        "Recepción 24h muy eficiente."),
                                        new Valoracion(hoteles.get(1), clientes.get(2),
                                                        LocalDate.now().minusDays(19), 9,
                                                        "Vistas a la Catedral desde la ventana."),
                                        new Valoracion(hoteles.get(1), clientes.get(3),
                                                        LocalDate.now().minusMonths(1).minusDays(27), 8,
                                                        "Acceso a puntos turísticos perfecto."),
                                        // Media: 8.5

                                        // --- Hotel 3: Rías Baixas Resort ---
                                        new Valoracion(hoteles.get(2), clientes.get(8),
                                                        LocalDate.now().minusDays(15), 6, "Bien, pero algo caro."),
                                        new Valoracion(hoteles.get(2), clientes.get(9),
                                                        LocalDate.now().minusMonths(1).minusDays(4), 5,
                                                        "Limpieza mejorable."),
                                        new Valoracion(hoteles.get(2), clientes.get(0),
                                                        LocalDate.now().minusDays(7), 7, "Spa muy bueno."),
                                        new Valoracion(hoteles.get(2), clientes.get(1),
                                                        LocalDate.now().minusMonths(2).plusDays(3), 4,
                                                        "No cumplió mis expectativas."),
                                        new Valoracion(hoteles.get(2), clientes.get(2),
                                                        LocalDate.now().minusDays(55), 6,
                                                        "Piscina al aire libre agradable."),
                                        new Valoracion(hoteles.get(2), clientes.get(3),
                                                        LocalDate.now().minusDays(26), 5,
                                                        "Mariscos frescos en el restaurante."),
                                        new Valoracion(hoteles.get(2), clientes.get(4),
                                                        LocalDate.now().minusMonths(3).minusDays(2), 6,
                                                        "Jardines bien cuidados."),
                                        new Valoracion(hoteles.get(2), clientes.get(5),
                                                        LocalDate.now().minusDays(44), 5,
                                                        "Precio alto respecto a servicios."),
                                        new Valoracion(hoteles.get(2), clientes.get(6),
                                                        LocalDate.now().minusDays(30), 7,
                                                        "Acceso a actividades acuáticas bueno."),
                                        new Valoracion(hoteles.get(2), clientes.get(7),
                                                        LocalDate.now().minusMonths(2).minusDays(19), 5,
                                                        "Personal poco ágil en servicio."),
                                        new Valoracion(hoteles.get(2), clientes.get(8),
                                                        LocalDate.now().minusDays(11), 6,
                                                        "Habitación con humedad detectada."),
                                        // Media: 5.5

                                        // --- Hotel 4: Hotel Rural O Bosque Encantado ---
                                        new Valoracion(hoteles.get(3), clientes.get(2),
                                                        LocalDate.now().minusDays(8), 8,
                                                        "Muy tranquilo y rodeado de naturaleza."),
                                        new Valoracion(hoteles.get(3), clientes.get(3),
                                                        LocalDate.now().minusMonths(1).minusDays(6), 7,
                                                        "Ideal para desconectar."),
                                        new Valoracion(hoteles.get(3), clientes.get(4),
                                                        LocalDate.now().minusDays(2), 9,
                                                        "Excelente trato del personal."),
                                        new Valoracion(hoteles.get(3), clientes.get(5),
                                                        LocalDate.now().minusDays(25), 6, "Algo aislado."),
                                        new Valoracion(hoteles.get(3), clientes.get(6),
                                                        LocalDate.now().minusDays(39), 7,
                                                        "Desayuno con productos locales delicioso."),
                                        new Valoracion(hoteles.get(3), clientes.get(7),
                                                        LocalDate.now().minusDays(50), 8,
                                                        "Chimenea en la habitación muy acogedora."),
                                        new Valoracion(hoteles.get(3), clientes.get(8),
                                                        LocalDate.now().minusMonths(2).minusDays(10), 9,
                                                        "Senderos para explorar increíbles."),
                                        new Valoracion(hoteles.get(3), clientes.get(9),
                                                        LocalDate.now().minusDays(14), 7,
                                                        "Atmósfera calmada y revitalizadora."),
                                        new Valoracion(hoteles.get(3), clientes.get(0),
                                                        LocalDate.now().minusDays(66), 8,
                                                        "Rutas de senderismo espectaculares."),
                                        new Valoracion(hoteles.get(3), clientes.get(1),
                                                        LocalDate.now().minusMonths(3).minusDays(21), 7,
                                                        "Comida casera exquisita."),
                                        new Valoracion(hoteles.get(3), clientes.get(2),
                                                        LocalDate.now().minusDays(36), 8,
                                                        "Piscina natural nearby es increíble."),
                                        new Valoracion(hoteles.get(3), clientes.get(1),
                                                        LocalDate.now().minusDays(18), 9,
                                                        "Servicio excelente, volveremos."),
                                        new Valoracion(hoteles.get(3), clientes.get(2),
                                                        LocalDate.now().minusDays(60), 7,
                                                        "Buen precio para la zona."),
                                        // Media: 7.5

                                        // --- Hotel 5: Galicia Luxury Suites ---
                                        new Valoracion(hoteles.get(4), clientes.get(6),
                                                        LocalDate.now().minusDays(11), 9, "Servicio excepcional."),
                                        new Valoracion(hoteles.get(4), clientes.get(7),
                                                        LocalDate.now().minusMonths(3).plusDays(1), 10,
                                                        "Todo perfecto."),
                                        new Valoracion(hoteles.get(4), clientes.get(8),
                                                        LocalDate.now().minusDays(6), 8, "Muy buenas instalaciones."),
                                        new Valoracion(hoteles.get(4), clientes.get(9),
                                                        LocalDate.now().minusMonths(2), 7, "Habitación amplia."),
                                        new Valoracion(hoteles.get(4), clientes.get(0),
                                                        LocalDate.now().minusDays(14), 9, "Muy recomendable."),
                                        new Valoracion(hoteles.get(4), clientes.get(1),
                                                        LocalDate.now().minusDays(37), 10,
                                                        "Spa privado de lujo."),
                                        new Valoracion(hoteles.get(4), clientes.get(2),
                                                        LocalDate.now().minusMonths(4).minusDays(3), 9,
                                                        "Servicio de mayordomo impecable."),
                                        new Valoracion(hoteles.get(4), clientes.get(3),
                                                        LocalDate.now().minusDays(28), 8,
                                                        "Restaurante gourmet excepcional."),
                                        new Valoracion(hoteles.get(4), clientes.get(4),
                                                        LocalDate.now().minusDays(11), 9,
                                                        "Vistas espectaculares sobre la ría."),
                                        new Valoracion(hoteles.get(4), clientes.get(5),
                                                        LocalDate.now().minusDays(45), 10,
                                                        "Lujo y autenticidad gallega combinados."),
                                        new Valoracion(hoteles.get(4), clientes.get(6),
                                                        LocalDate.now().minusDays(8), 9,
                                                        "Actividades culturales muy enriquecedoras."),
                                        new Valoracion(hoteles.get(4), clientes.get(7),
                                                        LocalDate.now().minusDays(31), 10,
                                                        "Personalización total en el servicio."),
                                        new Valoracion(hoteles.get(4), clientes.get(8),
                                                        LocalDate.now().minusMonths(2).minusDays(22), 9,
                                                        "Cena exclusiva fue inolvidable."),
                                        // Media: 8.6

                                        // --- Hotel 6: Hotel Costa Verde ---
                                        new Valoracion(hoteles.get(5), clientes.get(1),
                                                        LocalDate.now().minusDays(9), 7, "Acogedor, buena comida."),
                                        new Valoracion(hoteles.get(5), clientes.get(2),
                                                        LocalDate.now().minusDays(30), 6, "Habitación correcta."),
                                        new Valoracion(hoteles.get(5), clientes.get(3),
                                                        LocalDate.now().minusMonths(1).plusDays(2), 8,
                                                        "Vistas increíbles."),
                                        new Valoracion(hoteles.get(5), clientes.get(4),
                                                        LocalDate.now().minusDays(4), 7,
                                                        "Buena relación calidad-precio."),
                                        new Valoracion(hoteles.get(5), clientes.get(5),
                                                        LocalDate.now().minusDays(52), 7,
                                                        "Terrazas perfectas para atardeceres."),
                                        new Valoracion(hoteles.get(5), clientes.get(6),
                                                        LocalDate.now().minusDays(17), 6,
                                                        "Senderos costeros bien mantenidos."),
                                        new Valoracion(hoteles.get(5), clientes.get(7),
                                                        LocalDate.now().minusMonths(3).minusDays(8), 8,
                                                        "Decoración náutica muy bonita."),
                                        new Valoracion(hoteles.get(5), clientes.get(8),
                                                        LocalDate.now().minusDays(23), 7,
                                                        "Bar ideal para disfrutar el oleaje."),
                                        new Valoracion(hoteles.get(5), clientes.get(9),
                                                        LocalDate.now().minusDays(47), 8,
                                                        "Sunset desde la terraza perfecto."),
                                        new Valoracion(hoteles.get(5), clientes.get(0),
                                                        LocalDate.now().minusDays(32), 6,
                                                        "Servicio de limpieza podría mejorar."),
                                        new Valoracion(hoteles.get(5), clientes.get(1),
                                                        LocalDate.now().minusMonths(2).minusDays(35), 7,
                                                        "Desayuno con productos locales."),
                                        // Media: 7.0

                                        // --- Hotel 7: Pazo do Río Boutique ---
                                        new Valoracion(hoteles.get(6), clientes.get(5),
                                                        LocalDate.now().minusDays(17), 8, "Precioso entorno."),
                                        new Valoracion(hoteles.get(6), clientes.get(6),
                                                        LocalDate.now().minusMonths(2).minusDays(1), 9,
                                                        "Muy elegante."),
                                        new Valoracion(hoteles.get(6), clientes.get(7),
                                                        LocalDate.now().minusDays(3), 8, "Buen desayuno."),
                                        new Valoracion(hoteles.get(6), clientes.get(8),
                                                        LocalDate.now().minusDays(22), 7, "Cama cómoda."),
                                        new Valoracion(hoteles.get(6), clientes.get(9),
                                                        LocalDate.now().minusDays(31), 8,
                                                        "Elementos arquitectónicos nobles."),
                                        new Valoracion(hoteles.get(6), clientes.get(0),
                                                        LocalDate.now().minusMonths(3).plusDays(7), 9,
                                                        "Estanques y fuentes hermosos."),
                                        new Valoracion(hoteles.get(6), clientes.get(1),
                                                        LocalDate.now().minusDays(40), 8,
                                                        "Vinos locales excepcionales."),
                                        new Valoracion(hoteles.get(6), clientes.get(2),
                                                        LocalDate.now().minusDays(54), 7,
                                                        "Ideal para escapadas románticas."),
                                        new Valoracion(hoteles.get(6), clientes.get(3),
                                                        LocalDate.now().minusDays(27), 9,
                                                        "Experiencia medieval transportada."),
                                        new Valoracion(hoteles.get(6), clientes.get(4),
                                                        LocalDate.now().minusMonths(1).minusDays(42), 8,
                                                        "Personal uniforme elegante y educado."),
                                        new Valoracion(hoteles.get(6), clientes.get(5),
                                                        LocalDate.now().minusDays(18), 8,
                                                        "Lectura nocturna en biblioteca acogedora."),
                                        new Valoracion(hoteles.get(6), clientes.get(0),
                                                        LocalDate.now().minusDays(12), 8,
                                                        "Buena relación calidad-precio."),
                                        new Valoracion(hoteles.get(6), clientes.get(7),
                                                        LocalDate.now().minusDays(90), 6,
                                                        "El servicio tardó en llegar."),
                                        // Media: 8.0

                                        // --- Hotel 8: Hotel Atlántico Norte ---
                                        new Valoracion(hoteles.get(7), clientes.get(9),
                                                        LocalDate.now().minusDays(5), 9, "Hotel moderno y cómodo."),
                                        new Valoracion(hoteles.get(7), clientes.get(0),
                                                        LocalDate.now().minusMonths(1), 10,
                                                        "Excelente en todos los sentidos."),
                                        new Valoracion(hoteles.get(7), clientes.get(1),
                                                        LocalDate.now().minusDays(28), 8, "Personal amable."),
                                        new Valoracion(hoteles.get(7), clientes.get(2),
                                                        LocalDate.now().minusMonths(2).plusDays(5), 9,
                                                        "Muy recomendable."),
                                        new Valoracion(hoteles.get(7), clientes.get(3),
                                                        LocalDate.now().minusDays(16), 7, "Algo caro."),
                                        new Valoracion(hoteles.get(7), clientes.get(4),
                                                        LocalDate.now().minusDays(36), 9,
                                                        "Escritorios perfectos para trabajar."),
                                        new Valoracion(hoteles.get(7), clientes.get(5),
                                                        LocalDate.now().minusMonths(3).minusDays(9), 10,
                                                        "Gimnasio bien equipado."),
                                        new Valoracion(hoteles.get(7), clientes.get(6),
                                                        LocalDate.now().minusDays(19), 8,
                                                        "Zonas comunes amplias y cómodas."),
                                        new Valoracion(hoteles.get(7), clientes.get(7),
                                                        LocalDate.now().minusDays(47), 9,
                                                        "Vistas a la ciudad portuaria hermosas."),
                                        new Valoracion(hoteles.get(7), clientes.get(8),
                                                        LocalDate.now().minusDays(11), 8,
                                                        "Acceso rápido a puntos estratégicos."),
                                        // Media: 8.6

                                        // --- Hotel 9: Monte do Silencio ---
                                        new Valoracion(hoteles.get(8), clientes.get(4),
                                                        LocalDate.now().minusDays(14), 7, "Muy tranquilo."),
                                        new Valoracion(hoteles.get(8), clientes.get(5),
                                                        LocalDate.now().minusMonths(1).plusDays(2), 6, "Correcto."),
                                        new Valoracion(hoteles.get(8), clientes.get(6),
                                                        LocalDate.now().minusDays(3), 8, "Naturaleza pura."),
                                        new Valoracion(hoteles.get(8), clientes.get(7),
                                                        LocalDate.now().minusDays(19), 7,
                                                        "Buen sitio para desconectar."),
                                        new Valoracion(hoteles.get(8), clientes.get(8),
                                                        LocalDate.now().minusDays(41), 6,
                                                        "Materiales sostenibles en todo."),
                                        new Valoracion(hoteles.get(8), clientes.get(9),
                                                        LocalDate.now().minusMonths(2).minusDays(13), 7,
                                                        "Yoga al amanecer relajante."),
                                        new Valoracion(hoteles.get(8), clientes.get(0),
                                                        LocalDate.now().minusDays(25), 8,
                                                        "Cielo nocturno espectacular."),
                                        new Valoracion(hoteles.get(8), clientes.get(1),
                                                        LocalDate.now().minusDays(58), 7,
                                                        "Menús con ingredientes de temporada."),
                                        new Valoracion(hoteles.get(8), clientes.get(2),
                                                        LocalDate.now().minusDays(29), 8,
                                                        "Meditación guiada excelente."),
                                        new Valoracion(hoteles.get(8), clientes.get(3),
                                                        LocalDate.now().minusMonths(2).minusDays(8), 6,
                                                        "Precio algo elevado por servicios."),
                                        // Media: 7.0

                                        // --- Hotel 10: Hotel do Mar ---
                                        new Valoracion(hoteles.get(9), clientes.get(8),
                                                        LocalDate.now().minusDays(4), 9, "Muy luminoso."),
                                        new Valoracion(hoteles.get(9), clientes.get(9),
                                                        LocalDate.now().minusDays(12), 8, "Desayuno muy bueno."),
                                        new Valoracion(hoteles.get(9), clientes.get(0),
                                                        LocalDate.now().minusMonths(1).minusDays(3), 7,
                                                        "Buen servicio."),
                                        new Valoracion(hoteles.get(9), clientes.get(1),
                                                        LocalDate.now().minusDays(11), 8, "Cerca del puerto."),
                                        new Valoracion(hoteles.get(9), clientes.get(2),
                                                        LocalDate.now().minusDays(43), 8,
                                                        "Mariscos frescos absolutamente frescos."),
                                        new Valoracion(hoteles.get(9), clientes.get(3),
                                                        LocalDate.now().minusDays(27), 9,
                                                        "Pescado de la mejor calidad."),
                                        new Valoracion(hoteles.get(9), clientes.get(4),
                                                        LocalDate.now().minusMonths(3).minusDays(6), 8,
                                                        "Terraza con vista del puerto."),
                                        new Valoracion(hoteles.get(9), clientes.get(5),
                                                        LocalDate.now().minusDays(34), 7,
                                                        "Punto de partida perfecto para excursiones."),
                                        new Valoracion(hoteles.get(9), clientes.get(6),
                                                        LocalDate.now().minusDays(53), 9,
                                                        "Vinos gallegos premium en el bar."),
                                        new Valoracion(hoteles.get(9), clientes.get(7),
                                                        LocalDate.now().minusMonths(1).minusDays(35), 8,
                                                        "Música en vivo durante cenas."),
                                        // Media: 8.0

                                        // --- Hotel 11: Camiño Real Inn ---
                                        new Valoracion(hoteles.get(10), clientes.get(2),
                                                        LocalDate.now().minusDays(9), 8, "Buen alojamiento."),
                                        new Valoracion(hoteles.get(10), clientes.get(3),
                                                        LocalDate.now().minusMonths(1), 7, "Ambiente acogedor."),
                                        new Valoracion(hoteles.get(10), clientes.get(4),
                                                        LocalDate.now().minusDays(18), 9, "Perfecto para peregrinos."),
                                        new Valoracion(hoteles.get(10), clientes.get(5),
                                                        LocalDate.now().minusDays(2), 6, "Algo ruidoso."),
                                        new Valoracion(hoteles.get(10), clientes.get(6),
                                                        LocalDate.now().minusDays(38), 8,
                                                        "Desayunos tempranos bien atendidos."),
                                        new Valoracion(hoteles.get(10), clientes.get(7),
                                                        LocalDate.now().minusMonths(2).minusDays(14), 7,
                                                        "Información turística muy completa."),
                                        new Valoracion(hoteles.get(10), clientes.get(8),
                                                        LocalDate.now().minusDays(24), 9,
                                                        "Rutas históricas bien señaladas."),
                                        new Valoracion(hoteles.get(10), clientes.get(9),
                                                        LocalDate.now().minusDays(60), 6,
                                                        "Habitaciones funcionales aunque sencillas."),
                                        new Valoracion(hoteles.get(10), clientes.get(0),
                                                        LocalDate.now().minusDays(19), 8,
                                                        "Comunidad de peregrinos muy solidaria."),
                                        new Valoracion(hoteles.get(10), clientes.get(1),
                                                        LocalDate.now().minusMonths(2).minusDays(28), 7,
                                                        "Mapas del Camino muy detallados.")
                        // Media: 7.5
                        );

                        uRep.save(admin);
                        // Guardar y usar las instancias persistidas (gestionadas) para evitar
                        // detached entity exceptions cuando se referencien más abajo.
                        clientes = cRep.saveAll(clientes);
                        empresas = eRep.saveAll(empresas);
                        hoteles = hoRep.saveAll(hoteles);
                        habitaciones = haRep.saveAll(habitaciones);
                        vaRep.saveAll(valoraciones);

                        // Reservas
                        List<Reserva> reservas = new ArrayList<>();

                        // Helper para crear Reserva y sus DetalleReserva (mantiene referencias
                        // consistentes)
                        BiFunction<Reserva, List<DetalleReserva>, Reserva> attachDetalles = (r, detalles) -> {
                                detalles.forEach(d -> d.setReserva(r));
                                r.setHabitaciones(new ArrayList<>(detalles));
                                return r;
                        };

                        // Today base
                        LocalDate today = LocalDate.now();

                        // 1) Reserva simple 1 noche, Hotel 0, cliente 0, ocupa 1 habitación doble (hab
                        // index 0)
                        Reserva r1 = Reserva.builder()
                                        .fechaInicio(today.plusDays(1))
                                        .fechaFin(today.plusDays(2))
                                        .personas(2)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(0))
                                        .hotel(hoteles.get(0))
                                        .build();
                        DetalleReserva dr11 = new DetalleReserva(habitaciones.get(0), null, 1,
                                        habitaciones.get(0).getNombre(), habitaciones.get(0).getPrecio());
                        attachDetalles.apply(r1, List.of(dr11));
                        reservas.add(r1);

                        // 2) Reserva que ocupa toda la "Suite Familiar" (cantidad 5) en Hotel 0 —
                        // ocupación total en varias DetalleReserva (una sola reserva ocupa todo)
                        Reserva r2 = Reserva.builder()
                                        .fechaInicio(today.plusDays(2))
                                        .fechaFin(today.plusDays(5))
                                        .personas(12) // familia grande
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(1))
                                        .hotel(hoteles.get(0))
                                        .build();
                        DetalleReserva dr21 = new DetalleReserva(habitaciones.get(1), null, 5,
                                        habitaciones.get(1).getNombre(), habitaciones.get(1).getPrecio());
                        attachDetalles.apply(r2, List.of(dr21));
                        reservas.add(r2);

                        // 3) Reserva corta en Hotel 1, 2 habitaciones Doble Estándar
                        // (habitaciones.get(3))
                        Reserva r3 = Reserva.builder()
                                        .fechaInicio(today.plusDays(1))
                                        .fechaFin(today.plusDays(3))
                                        .personas(4)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(2))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr31 = new DetalleReserva(habitaciones.get(3), null, 2,
                                        habitaciones.get(3).getNombre(), habitaciones.get(3).getPrecio());
                        attachDetalles.apply(r3, List.of(dr31));
                        reservas.add(r3);

                        // 4) Reserva en Hotel 1 que usa Suite Presidencial (cantidad 2) — ocupa todo el
                        // stock (2)
                        Reserva r4 = Reserva.builder()
                                        .fechaInicio(today.plusDays(3))
                                        .fechaFin(today.plusDays(6))
                                        .personas(4)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(3))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr41 = new DetalleReserva(habitaciones.get(6), null, 2,
                                        habitaciones.get(6).getNombre(), habitaciones.get(6).getPrecio());
                        attachDetalles.apply(r4, List.of(dr41));
                        reservas.add(r4);

                        // 5) Reserva en Hotel 2 (habitaciones index 7 y 8), dos tipos distintos en la
                        // misma reserva
                        Reserva r5 = Reserva.builder()
                                        .fechaInicio(today.plusDays(4))
                                        .fechaFin(today.plusDays(6))
                                        .personas(4)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(4))
                                        .hotel(hoteles.get(2))
                                        .build();
                        DetalleReserva dr51 = new DetalleReserva(habitaciones.get(7), null, 1,
                                        habitaciones.get(7).getNombre(), habitaciones.get(7).getPrecio());
                        DetalleReserva dr52 = new DetalleReserva(habitaciones.get(8), null, 1,
                                        habitaciones.get(8).getNombre(), habitaciones.get(8).getPrecio());
                        attachDetalles.apply(r5, List.of(dr51, dr52));
                        reservas.add(r5);

                        // 6) Reserva que ocupa 3 bungalows familiares (cantidad 5 hay), reserva parcial
                        Reserva r6 = Reserva.builder()
                                        .fechaInicio(today.plusDays(2))
                                        .fechaFin(today.plusDays(4))
                                        .personas(10)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(5))
                                        .hotel(hoteles.get(2))
                                        .build();
                        DetalleReserva dr61 = new DetalleReserva(habitaciones.get(10), null, 3,
                                        habitaciones.get(10).getNombre(), habitaciones.get(10).getPrecio());
                        attachDetalles.apply(r6, List.of(dr61));
                        reservas.add(r6);

                        // 7) Reserva en Hotel 3, ocupa Suite Ribeira Sacra (cantidad 3) — ocupa todo
                        // stock 3 en 1 reserva (ejemplo de ocupación total)
                        Reserva r7 = Reserva.builder()
                                        .fechaInicio(today.plusDays(5))
                                        .fechaFin(today.plusDays(8))
                                        .personas(6)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(6))
                                        .hotel(hoteles.get(3))
                                        .build();
                        DetalleReserva dr71 = new DetalleReserva(habitaciones.get(12), null, 3,
                                        habitaciones.get(12).getNombre(), habitaciones.get(12).getPrecio());
                        attachDetalles.apply(r7, List.of(dr71));
                        reservas.add(r7);

                        // 8) Reserva en Hotel 3, habitación familiar rural (cantidad 4) — reserva
                        // pequeña
                        Reserva r8 = Reserva.builder()
                                        .fechaInicio(today.plusDays(7))
                                        .fechaFin(today.plusDays(9))
                                        .personas(4)
                                        .estado(EstadoReserva.FINALIZADA)
                                        .cliente(clientes.get(7))
                                        .hotel(hoteles.get(3))
                                        .build();
                        DetalleReserva dr81 = new DetalleReserva(habitaciones.get(13), null, 1,
                                        habitaciones.get(13).getNombre(), habitaciones.get(13).getPrecio());
                        attachDetalles.apply(r8, List.of(dr81));
                        reservas.add(r8);

                        // 9) Reserva en Hotel 4 (suite deluxe), usa 2 suites (cantidad 8 disponible)
                        Reserva r9 = Reserva.builder()
                                        .fechaInicio(today.plusDays(1))
                                        .fechaFin(today.plusDays(4))
                                        .personas(4)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(8))
                                        .hotel(hoteles.get(4))
                                        .build();
                        DetalleReserva dr91 = new DetalleReserva(habitaciones.get(14), null, 2,
                                        habitaciones.get(14).getNombre(), habitaciones.get(14).getPrecio());
                        attachDetalles.apply(r9, List.of(dr91));
                        reservas.add(r9);

                        // 10) Reserva en Hotel 4 que ocupa Suite Presidencial Vigo (cantidad 2) — ocupa
                        // todo y se solapa con r9 dates (ejemplo de consumo)
                        Reserva r10 = Reserva.builder()
                                        .fechaInicio(today.plusDays(2))
                                        .fechaFin(today.plusDays(5))
                                        .personas(4)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(9))
                                        .hotel(hoteles.get(4))
                                        .build();
                        DetalleReserva dr101 = new DetalleReserva(habitaciones.get(17), null, 2,
                                        habitaciones.get(17).getNombre(), habitaciones.get(17).getPrecio());
                        attachDetalles.apply(r10, List.of(dr101));
                        reservas.add(r10);

                        // 11) Reserva en Hotel 5 (Hotel Costa Verde): 1 habitación doble (index 18)
                        Reserva r11 = Reserva.builder()
                                        .fechaInicio(today.plusDays(3))
                                        .fechaFin(today.plusDays(4))
                                        .personas(2)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(0))
                                        .hotel(hoteles.get(5))
                                        .build();
                        DetalleReserva dr111 = new DetalleReserva(habitaciones.get(18), null, 1,
                                        habitaciones.get(18).getNombre(), habitaciones.get(18).getPrecio());
                        attachDetalles.apply(r11, List.of(dr111));
                        reservas.add(r11);

                        // 12) Reserva en Hotel 5: Suite con Terraza Panorámica (cantidad 3) — consumo
                        // parcial (3 ocupadas en total con otras reservas)
                        Reserva r12 = Reserva.builder()
                                        .fechaInicio(today.plusDays(4))
                                        .fechaFin(today.plusDays(7))
                                        .personas(2)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(1))
                                        .hotel(hoteles.get(5))
                                        .build();
                        DetalleReserva dr121 = new DetalleReserva(habitaciones.get(19), null, 1,
                                        habitaciones.get(19).getNombre(), habitaciones.get(19).getPrecio());
                        attachDetalles.apply(r12, List.of(dr121));
                        reservas.add(r12);

                        // 13) Reserva en Hotel 6 (Pazo do Río Boutique), ocupa "Habitación Doble
                        // Clásica" (index 22)
                        Reserva r13 = Reserva.builder()
                                        .fechaInicio(today) // ajustada a hoy para cumplir @FutureOrPresent (lo olvidé)
                                        .fechaFin(today.plusDays(1))
                                        .personas(2)
                                        .estado(EstadoReserva.FINALIZADA)
                                        .cliente(clientes.get(2))
                                        .hotel(hoteles.get(6))
                                        .build();
                        DetalleReserva dr131 = new DetalleReserva(habitaciones.get(22), null, 1,
                                        habitaciones.get(22).getNombre(), habitaciones.get(22).getPrecio());
                        attachDetalles.apply(r13, List.of(dr131));
                        reservas.add(r13);

                        // 14) Reserva en Hotel 6: Suite del Pazo (index 23), ocupa 3 (cantidad 3) ->
                        // ocupa todo el stock
                        Reserva r14 = Reserva.builder()
                                        .fechaInicio(today.plusDays(2))
                                        .fechaFin(today.plusDays(4))
                                        .personas(6)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(3))
                                        .hotel(hoteles.get(6))
                                        .build();
                        DetalleReserva dr141 = new DetalleReserva(habitaciones.get(23), null, 3,
                                        habitaciones.get(23).getNombre(), habitaciones.get(23).getPrecio());
                        attachDetalles.apply(r14, List.of(dr141));
                        reservas.add(r14);

                        // 15) Reserva en Hotel 7 (Hotel Atlántico Norte), usa 1 habitación Individual
                        // Confort (index 25)
                        Reserva r15 = Reserva.builder()
                                        .fechaInicio(today.plusDays(1))
                                        .fechaFin(today.plusDays(2))
                                        .personas(1)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(4))
                                        .hotel(hoteles.get(7))
                                        .build();
                        DetalleReserva dr151 = new DetalleReserva(habitaciones.get(25), null, 1,
                                        habitaciones.get(25).getNombre(), habitaciones.get(25).getPrecio());
                        attachDetalles.apply(r15, List.of(dr151));
                        reservas.add(r15);

                        // 16) Reserva en Hotel 7: Suite Ejecutiva (index 27), ocupa 2 (cantidad 4)
                        Reserva r16 = Reserva.builder()
                                        .fechaInicio(today.plusDays(6))
                                        .fechaFin(today.plusDays(9))
                                        .personas(4)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(5))
                                        .hotel(hoteles.get(7))
                                        .build();
                        DetalleReserva dr161 = new DetalleReserva(habitaciones.get(27), null, 2,
                                        habitaciones.get(27).getNombre(), habitaciones.get(27).getPrecio());
                        attachDetalles.apply(r16, List.of(dr161));
                        reservas.add(r16);

                        // 17) Reserva en Hotel 8 (Monte do Silencio): Cabaña de Madera (index 29),
                        // ocupa 2 (cantidad 4)
                        Reserva r17 = Reserva.builder()
                                        .fechaInicio(today.plusDays(3))
                                        .fechaFin(today.plusDays(6))
                                        .personas(4)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(6))
                                        .hotel(hoteles.get(8))
                                        .build();
                        DetalleReserva dr171 = new DetalleReserva(habitaciones.get(29), null, 2,
                                        habitaciones.get(29).getNombre(), habitaciones.get(29).getPrecio());
                        attachDetalles.apply(r17, List.of(dr171));
                        reservas.add(r17);

                        // 18) Reserva en Hotel 9 (Hotel do Mar): Suite Frente al Mar (index 33), ocupa
                        // 1
                        Reserva r18 = Reserva.builder()
                                        .fechaInicio(today.plusDays(8))
                                        .fechaFin(today.plusDays(10))
                                        .personas(2)
                                        .estado(EstadoReserva.CANCELADA)
                                        .cliente(clientes.get(7))
                                        .hotel(hoteles.get(9))
                                        .build();
                        DetalleReserva dr181 = new DetalleReserva(habitaciones.get(33), null, 1,
                                        habitaciones.get(33).getNombre(), habitaciones.get(33).getPrecio());
                        attachDetalles.apply(r18, List.of(dr181));
                        reservas.add(r18);

                        // 19) Reserva en Hotel 10 (Camiño Real Inn): Habitacion Individual Peregrino
                        // (index 36), ocupa 3 (cantidad 8)
                        Reserva r19 = Reserva.builder()
                                        .fechaInicio(today.plusDays(1))
                                        .fechaFin(today.plusDays(3))
                                        .personas(3)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(8))
                                        .hotel(hoteles.get(10))
                                        .build();
                        DetalleReserva dr19 = new DetalleReserva(
                                        habitaciones.get(35), // última habitación
                                        null,
                                        3,
                                        habitaciones.get(35).getNombre(),
                                        habitaciones.get(35).getPrecio());
                        attachDetalles.apply(r19, List.of(dr19));
                        reservas.add(r19);

                        // 20) Reserva combinada en Hotel 10: Hab. Familiar Peregrinos (index 38) ocupa
                        // 2
                        Reserva r20 = Reserva.builder()
                                        .fechaInicio(today.plusDays(4))
                                        .fechaFin(today.plusDays(6))
                                        .personas(8)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(9))
                                        .hotel(hoteles.get(10))
                                        .build();
                        DetalleReserva dr20 = new DetalleReserva(
                                        habitaciones.get(34), // penúltima habitación
                                        null,
                                        2,
                                        habitaciones.get(34).getNombre(),
                                        habitaciones.get(34).getPrecio());
                        attachDetalles.apply(r20, List.of(dr20));
                        reservas.add(r20);

                        // ===== NUEVAS RESERVAS ADICIONALES =====

                        // Reservas adicionales para Hotel 1 (Gran Hotel Santiago) - 6 en diferentes
                        // estados
                        // 21) Hotel 1 - REALIZADA
                        Reserva r21 = Reserva.builder()
                                        .fechaInicio(today.minusDays(5))
                                        .fechaFin(today.minusDays(3))
                                        .personas(2)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(0))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr211 = new DetalleReserva(habitaciones.get(3), null, 1,
                                        habitaciones.get(3).getNombre(), habitaciones.get(3).getPrecio());
                        attachDetalles.apply(r21, List.of(dr211));
                        reservas.add(r21);

                        // 22) Hotel 1 - CONFIRMADA
                        Reserva r22 = Reserva.builder()
                                        .fechaInicio(today.plusDays(10))
                                        .fechaFin(today.plusDays(12))
                                        .personas(3)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(1))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr221 = new DetalleReserva(habitaciones.get(4), null, 1,
                                        habitaciones.get(4).getNombre(), habitaciones.get(4).getPrecio());
                        attachDetalles.apply(r22, List.of(dr221));
                        reservas.add(r22);

                        // 23) Hotel 1 - FINALIZADA
                        Reserva r23 = Reserva.builder()
                                        .fechaInicio(today.minusDays(15))
                                        .fechaFin(today.minusDays(10))
                                        .personas(4)
                                        .estado(EstadoReserva.FINALIZADA)
                                        .cliente(clientes.get(2))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr231 = new DetalleReserva(habitaciones.get(5), null, 1,
                                        habitaciones.get(5).getNombre(), habitaciones.get(5).getPrecio());
                        attachDetalles.apply(r23, List.of(dr231));
                        reservas.add(r23);

                        // 24) Hotel 1 - CANCELADA
                        Reserva r24 = Reserva.builder()
                                        .fechaInicio(today.plusDays(20))
                                        .fechaFin(today.plusDays(22))
                                        .personas(2)
                                        .estado(EstadoReserva.CANCELADA)
                                        .cliente(clientes.get(3))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr241 = new DetalleReserva(habitaciones.get(3), null, 1,
                                        habitaciones.get(3).getNombre(), habitaciones.get(3).getPrecio());
                        attachDetalles.apply(r24, List.of(dr241));
                        reservas.add(r24);

                        // 25) Hotel 1 - REALIZADA (otra)
                        Reserva r25 = Reserva.builder()
                                        .fechaInicio(today.minusDays(8))
                                        .fechaFin(today.minusDays(6))
                                        .personas(3)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(4))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr251 = new DetalleReserva(habitaciones.get(4), null, 1,
                                        habitaciones.get(4).getNombre(), habitaciones.get(4).getPrecio());
                        attachDetalles.apply(r25, List.of(dr251));
                        reservas.add(r25);

                        // 26) Hotel 1 - CONFIRMADA (otra)
                        Reserva r26 = Reserva.builder()
                                        .fechaInicio(today.plusDays(15))
                                        .fechaFin(today.plusDays(17))
                                        .personas(2)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(5))
                                        .hotel(hoteles.get(1))
                                        .build();
                        DetalleReserva dr261 = new DetalleReserva(habitaciones.get(5), null, 1,
                                        habitaciones.get(5).getNombre(), habitaciones.get(5).getPrecio());
                        attachDetalles.apply(r26, List.of(dr261));
                        reservas.add(r26);

                        // Reservas adicionales para otros hoteles
                        // 27) Hotel 0 - Habitación Individual
                        Reserva r27 = Reserva.builder()
                                        .fechaInicio(today.plusDays(5))
                                        .fechaFin(today.plusDays(6))
                                        .personas(1)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(6))
                                        .hotel(hoteles.get(0))
                                        .build();
                        DetalleReserva dr271 = new DetalleReserva(habitaciones.get(2), null, 1,
                                        habitaciones.get(2).getNombre(), habitaciones.get(2).getPrecio());
                        attachDetalles.apply(r27, List.of(dr271));
                        reservas.add(r27);

                        // 28) Hotel 2 - Habitación Doble
                        Reserva r28 = Reserva.builder()
                                        .fechaInicio(today.plusDays(8))
                                        .fechaFin(today.plusDays(10))
                                        .personas(2)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(7))
                                        .hotel(hoteles.get(2))
                                        .build();
                        DetalleReserva dr281 = new DetalleReserva(habitaciones.get(7), null, 1,
                                        habitaciones.get(7).getNombre(), habitaciones.get(7).getPrecio());
                        attachDetalles.apply(r28, List.of(dr281));
                        reservas.add(r28);

                        // 29) Hotel 3 - Habitación Doble Rural
                        Reserva r29 = Reserva.builder()
                                        .fechaInicio(today.plusDays(12))
                                        .fechaFin(today.plusDays(14))
                                        .personas(2)
                                        .estado(EstadoReserva.FINALIZADA)
                                        .cliente(clientes.get(8))
                                        .hotel(hoteles.get(3))
                                        .build();
                        DetalleReserva dr291 = new DetalleReserva(habitaciones.get(11), null, 1,
                                        habitaciones.get(11).getNombre(), habitaciones.get(11).getPrecio());
                        attachDetalles.apply(r29, List.of(dr291));
                        reservas.add(r29);

                        // 30) Hotel 4 - Suite Deluxe
                        Reserva r30 = Reserva.builder()
                                        .fechaInicio(today.minusDays(3))
                                        .fechaFin(today.minusDays(1))
                                        .personas(2)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(9))
                                        .hotel(hoteles.get(4))
                                        .build();
                        DetalleReserva dr301 = new DetalleReserva(habitaciones.get(14), null, 1,
                                        habitaciones.get(14).getNombre(), habitaciones.get(14).getPrecio());
                        attachDetalles.apply(r30, List.of(dr301));
                        reservas.add(r30);

                        // 31) Hotel 5 - Habitación Doble
                        Reserva r31 = Reserva.builder()
                                        .fechaInicio(today.plusDays(18))
                                        .fechaFin(today.plusDays(20))
                                        .personas(2)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(0))
                                        .hotel(hoteles.get(5))
                                        .build();
                        DetalleReserva dr311 = new DetalleReserva(habitaciones.get(18), null, 1,
                                        habitaciones.get(18).getNombre(), habitaciones.get(18).getPrecio());
                        attachDetalles.apply(r31, List.of(dr311));
                        reservas.add(r31);

                        // 32) Hotel 6 - Suite del Pazo
                        Reserva r32 = Reserva.builder()
                                        .fechaInicio(today.plusDays(11))
                                        .fechaFin(today.plusDays(13))
                                        .personas(2)
                                        .estado(EstadoReserva.CONFIRMADA)
                                        .cliente(clientes.get(1))
                                        .hotel(hoteles.get(6))
                                        .build();
                        DetalleReserva dr321 = new DetalleReserva(habitaciones.get(23), null, 1,
                                        habitaciones.get(23).getNombre(), habitaciones.get(23).getPrecio());
                        attachDetalles.apply(r32, List.of(dr321));
                        reservas.add(r32);

                        // 33) Hotel 7 - Habitación Doble Estándar
                        Reserva r33 = Reserva.builder()
                                        .fechaInicio(today.plusDays(9))
                                        .fechaFin(today.plusDays(11))
                                        .personas(2)
                                        .estado(EstadoReserva.REALIZADA)
                                        .cliente(clientes.get(2))
                                        .hotel(hoteles.get(7))
                                        .build();
                        DetalleReserva dr331 = new DetalleReserva(habitaciones.get(26), null, 1,
                                        habitaciones.get(26).getNombre(), habitaciones.get(26).getPrecio());
                        attachDetalles.apply(r33, List.of(dr331));
                        reservas.add(r33);

                        // 34) Hotel 8 - Habitación Ecológica
                        Reserva r34 = Reserva.builder()
                                        .fechaInicio(today.minusDays(10))
                                        .fechaFin(today.minusDays(8))
                                        .personas(2)
                                        .estado(EstadoReserva.FINALIZADA)
                                        .cliente(clientes.get(3))
                                        .hotel(hoteles.get(8))
                                        .build();
                        DetalleReserva dr341 = new DetalleReserva(habitaciones.get(28), null, 1,
                                        habitaciones.get(28).getNombre(), habitaciones.get(28).getPrecio());
                        attachDetalles.apply(r34, List.of(dr341));
                        reservas.add(r34);

                        // 35) Hotel 9 - Habitación Doble Estándar
                        Reserva r35 = Reserva.builder()
                                        .fechaInicio(today.plusDays(7))
                                        .fechaFin(today.plusDays(8))
                                        .personas(2)
                                        .estado(EstadoReserva.CANCELADA)
                                        .cliente(clientes.get(4))
                                        .hotel(hoteles.get(9))
                                        .build();
                        DetalleReserva dr351 = new DetalleReserva(habitaciones.get(31), null, 1,
                                        habitaciones.get(31).getNombre(), habitaciones.get(31).getPrecio());
                        attachDetalles.apply(r35, List.of(dr351));
                        reservas.add(r35);

                        // Resultado: lista de 35 reservas

                        reRep.saveAll(reservas);

                });
        }
}