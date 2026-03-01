package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.SimpleHotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.EmptyListException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.ComparaPrecio;
import com.hotelgalicia.proyectohotelgalicia.modelos.ComparaPrecioValo;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private UsuarioRepository uRep;

    @Autowired
    private HotelRepository hoRep;

    @Autowired
    private EmpresaRepository eRep;

    @Autowired
    private FileStorageService fileserv;

    @Autowired
    private DetalleReservaRepository drRep;

    @Override
    public List<Hotel> listAllHotel() {
        return hoRep.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelMiniDTO> listSortedHotel(HotelSearchDTO dto, Pageable pageable) {
        if (dto.getNombre().isBlank())
            dto.setNombre("");
        if (dto.getDireccion().isBlank())
            dto.setDireccion("");

        if (dto.getPresupuestoMin() > dto.getPresupuestoMax()) {
            throw new IllegalArgumentException("El presupuesto minimo no puede ser superior al maximo.");
        }
        Page<Hotel> hoteles;
        if (dto.getMunicipio() != null && dto.getMunicipio() != Municipios.TODOS) {
            hoteles = hoRep
                    .findByNombreContainingIgnoreCaseAndMunicipioAndDireccionContainingIgnoreCaseAndEstado(
                            dto.getNombre(), dto.getMunicipio(), dto.getDireccion(), true, pageable);
        } else {
            hoteles = hoRep.findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCaseAndEstado(
                    dto.getNombre(), dto.getDireccion(), true, pageable);
        }

        Long dias = ChronoUnit.DAYS.between(dto.getFechaInicio(), dto.getFechaFin());
        if (dias <= 0) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }
        List<HotelMiniDTO> hotelesdto = new ArrayList<>();
        // 1. Extraer todos los IDs de habitaciones de los hoteles encontrados
        List<Long> idsHabitaciones = hoteles.stream()
                .flatMap(h -> h.getHabitaciones().stream())
                .map(Habitacion::getId)
                .toList();

        // 2. Consultar disponibilidad de TODAS de una vez y guardarlo en un Mapa
        // Retorna: Map<IdHabitacion, CantidadReservada>
        Map<Long, Integer> mapaReservas = drRep.sumAllByHabitacionIds(
                idsHabitaciones,
                List.of(EstadoReserva.REALIZADA, EstadoReserva.CONFIRMADA),
                dto.getFechaInicio(),
                dto.getFechaFin()).stream().collect(Collectors.toMap(
                        obj -> (Long) obj[0],
                        obj -> ((Long) obj[1]).intValue()));

        for (Hotel hotel : hoteles) {
            Habitacion habit = hotel.getHabitaciones().stream()
                    .filter(h -> {
                        int reservadas = mapaReservas.getOrDefault(h.getId(), 0);
                        int disponible = h.getCantidad() - reservadas;

                        long capacidadTotal = h.getCapacidad() * dto.getCantHabi();

                        return h.getPrecio() * dias >= dto.getPresupuestoMin()
                                && h.getPrecio() * dias <= dto.getPresupuestoMax()
                                && capacidadTotal >= dto.getPersonas()
                                && disponible >= dto.getCantHabi();
                    })
                    .max(Comparator.comparingLong(h -> h.getCapacidad()))
                    .orElse(null);

            if (habit != null) {
                HotelMiniDTO hoteldto = ConvertHotelToDTO(hotel);
                hoteldto.setHabitacion(ConvertHabToDTO(habit, dias));
                hotelesdto.add(hoteldto);
            }
        }
        if (hotelesdto.isEmpty()) {
            throw new EmptyListException("No se encontraron hoteles que coincidieran con los filtros de busqueda.");
        }

        switch (dto.getFiltro()) {
            case PRECIO_DESCENDENTE ->
                Collections.sort(hotelesdto, new ComparaPrecio().reversed());
            case PRECIO_ASCENDENTE ->
                Collections.sort(hotelesdto, new ComparaPrecio());
            case VALORACION_DESCENDENTE ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getPuntaje).reversed());
            case VALORACION_ASCENDENTE ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getPuntaje));
            case VALORACION_PRECIO_ASCENDENTE ->
                Collections.sort(hotelesdto, new ComparaPrecioValo());
            case MAS_VALORADOS ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getValoraciones).reversed());

            default -> {
            }

        }
        return new PageImpl<>(hotelesdto, pageable, hoteles.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelMiniDTO> listSortedHotel(SimpleHotelSearchDTO dto, Pageable pageable) {
        if (dto.getNombre().isBlank())
            dto.setNombre("");
        if (dto.getDireccion().isBlank())
            dto.setDireccion("");

        Page<Hotel> hoteles;
        if (dto.getMunicipio() != null && dto.getMunicipio() != Municipios.TODOS) {
            hoteles = hoRep
                    .findByNombreContainingIgnoreCaseAndMunicipioAndDireccionContainingIgnoreCase(
                            dto.getNombre(), dto.getMunicipio(), dto.getDireccion(), pageable);
        } else {
            hoteles = hoRep.findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCase(
                    dto.getNombre(), dto.getDireccion(), pageable);
        }

        List<HotelMiniDTO> hotelesdto = new ArrayList<>();

        for (Hotel hotel : hoteles) {
            HotelMiniDTO hoteldto = ConvertHotelToDTO(hotel);
            hoteldto.setHabitacion(null);
            hotelesdto.add(hoteldto);
        }
        if (hotelesdto.isEmpty()) {
            throw new EmptyListException("No se encontraron hoteles que coincidieran con los filtros de busqueda.");
        }
        switch (dto.getFiltro()) {
            case NOMBRE_ASCENDENTE ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getNombre));
            case NOMBRE_DESCENDENTE ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getNombre).reversed());
            case VALORACION_DESCENDENTE ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getPuntaje).reversed());
            case VALORACION_ASCENDENTE ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getPuntaje));
            case MAS_VALORADOS ->
                Collections.sort(hotelesdto, Comparator.comparing(HotelMiniDTO::getValoraciones).reversed());

            default -> {
            }

        }
        return new PageImpl<>(hotelesdto, pageable, hoteles.getTotalElements());
    }

    @Override
    public List<Hotel> listHotelByCorpo(Long id) {
        return hoRep.findByEmpresaId(id);
    }

    @Override
    @Transactional(readOnly = true) // para cargar las habitaciones.
    public Hotel getById(Long id) {
        return hoRep.findByIdPage(id).orElseThrow(() -> new RuntimeException("Error: Hotel no encontrado"));
    }

    @Override
    public Hotel agregar(HotelDTO hotel, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Hotel hotelfinal = new Hotel(null,
                hotel.getNombre().trim(),
                hotel.getDescripcion().trim(),
                hotel.getMunicipio(),
                hotel.getDireccion().trim(),
                hotel.getTelefono().trim(), null, true, 5d,
                eRep.findByCorreo(authentication.getName())
                        .orElseThrow(
                                () -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario.")),
                null, null, null);
        if (!file.isEmpty()) {
            String nombreImagen = fileserv.store(file, hotel.getNombre());
            hotelfinal.setImagen(nombreImagen);
        }
        try {
            return hoRep.save(hotelfinal);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al agregar el hotel: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al crear el hotel: " + e.getMessage());
        }
    }

    @Override
    public Hotel modificar(HotelDTO hotel, Long hotelid, MultipartFile file) {
        Hotel base = hoRep.findById(hotelid)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado."));
        verificarHotel(base);
        base.setNombre(hotel.getNombre());
        base.setDescripcion(hotel.getDescripcion());
        base.setMunicipio(hotel.getMunicipio());
        base.setDireccion(hotel.getDireccion());
        base.setTelefono(hotel.getTelefono());
        if (file != null && !file.isEmpty()) {
            try {
                String nombreImagen = fileserv.store(file, base.getNombre());
                if (base.getImagen() != null) {
                    fileserv.delete(base.getImagen());
                }
                base.setImagen(nombreImagen);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la imagen: " + e.getMessage());
            }
        }

        try {
            return hoRep.save(base);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al guardar los cambios: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public Hotel cambiarEstadoPorId(Long id) {
        Hotel hoteliminar = hoRep.findById(id).orElseThrow(() -> new RuntimeException("Error: Hotel no encontrado"));
        verificarHotel(hoteliminar);
        try {
            hoteliminar.setEstado(!hoteliminar.getEstado());
            return hoRep.save(hoteliminar);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Error al cambiar el estado del hotel: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }

    // Conversores y verificadores
    public void verificarHotel(Hotel hotel) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));

        if (usuario.getRol() == null)
            throw new PermissionDeniedException("Error: Tipo de usuario no encontrado.");

        switch (usuario.getRol()) {
            case ADMIN -> {
            }
            case CORPORATION -> {
                Empresa empresa = eRep.findById(usuario.getId())
                        .orElseThrow(() -> new RuntimeException("Empresa no encontrada."));
                if (!hotel.getEmpresa().getId().equals(empresa.getId())) {
                    throw new PermissionDeniedException("No posee permisos para modificar esta entidad.");
                }
            }
            case USER -> throw new PermissionDeniedException("No está autorizado para realizar esta acción.");
            default -> throw new RuntimeException("Error al verificar permisos de usuario.");
        }
    }

    public HotelMiniDTO ConvertHotelToDTO(Hotel hotel) {
        HotelMiniDTO hotelfinal = new HotelMiniDTO(hotel.getId(), hotel.getNombre(), hotel.getMunicipio(),
                hotel.getDireccion(), hotel.getImagen(), hotel.getPuntaje(), hotel.getValoracion().size(), null);

        return hotelfinal;
    }

    private HabitacionMiniDTO ConvertHabToDTO(Habitacion habitacion, Long dias) {
        HabitacionMiniDTO habitacionfinal = new HabitacionMiniDTO(habitacion.getNombre(),
                habitacion.getCapacidad(), habitacion.getPrecio() * dias);
        return habitacionfinal;
    }
}
