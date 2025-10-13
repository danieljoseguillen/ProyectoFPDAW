package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.RoomFullException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private UsuarioRepository uRep;

    @Autowired
    private HotelRepository hoRep;

    @Autowired
    private HabitacionRepository haRep;

    @Autowired
    private EmpresaRepository eRep;

    @Autowired
    public fileStorageService fileserv;

    @Autowired
    private DetalleReservaRepository drRep;

    @Override
    public List<Hotel> listAllHotel() {
        return hoRep.findAll();
    }

    @Override
    public List<HotelMiniDTO> listSortedHotel(HotelSearchDTO dto) {
        if (dto.getNombre().isBlank()) {
            dto.setNombre("");
        }
        if (dto.getDireccion().isBlank()) {
            dto.setDireccion("");
        }

        if (dto.getPresupuestoMin() > dto.getPresupuestoMax()) {
            throw new IllegalArgumentException("El presupuesto minimo no puede ser superior al maximo.");
        }
        List<Hotel> hoteles;
        if (dto.getMunicipio() != null) {
            hoteles = hoRep
                    .findByNombreContainingIgnoreCaseAndMunicipioAndDireccionContainingIgnoreCaseAndEstado(
                            dto.getNombre(), dto.getMunicipio(), dto.getDireccion(), true);
        } else {
            hoteles = hoRep.findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCaseAndEstado(
                    dto.getNombre(), dto.getDireccion(), true);
        }
        Long dias = ChronoUnit.DAYS.between(dto.getFechaInicio(), dto.getFechaFin());
        List<HotelMiniDTO> hotelesdto = new ArrayList<>();
        for (Hotel hotel : hoteles) {


        }
        if (hotelesdto.isEmpty()) {
            List<Hotel> hotelesfull = hoRep.findByEstado(true);

            // Double maximo =
            // Collections.max(haRep.findByEstado(EstadoHabitacion.DISPONIBLE),
            // Comparator.comparingDouble(Habitacion::getPrecio)).getPrecio();
            // Double minimo =
            // Collections.min(haRep.findByEstado(EstadoHabitacion.DISPONIBLE),
            // Comparator.comparingDouble(Habitacion::getPrecio)).getPrecio();
            for (Hotel hotel : hotelesfull) {
                hotelesdto.add(ConvertHotelToDTO(hotel));
            }
            return hotelesdto;
        }
        // Double maximo = Collections.max(hotelesdto,
        // Comparator.comparingDouble(HotelMiniDTO::getCosto)).getCosto();
        // Double minimo = Collections.min(hotelesdto,
        // Comparator.comparingDouble(HotelMiniDTO::getCosto)).getCosto();
        // SearchResultDTO resultado = new SearchResultDTO(minimo, maximo,
        // hotelesdto.size(), hotelesdto);
        return hotelesdto;
    }

    @Override
    public List<Hotel> listHotelByCorpo(Long id) {
        return hoRep.findByEmpresaId(id);
    }

    @Override
    public Hotel getById(Long id) {
        return hoRep.findById(id).orElseThrow(() -> new RuntimeException("Error: Hotel no encontrado"));
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
                        .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado.")),
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
                if (base.getImagen() != null) {
                    fileserv.delete(base.getImagen());
                }
                String nombreImagen = fileserv.store(file, base.getNombre());
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
    public Hotel cambiarEstadoPorId(Long id, boolean estado) {
        Hotel hoteliminar = hoRep.findById(id).orElseThrow(() -> new RuntimeException("Error: Hotel no encontrado"));
        verificarHotel(hoteliminar);
        try {
            hoteliminar.setEstado(estado);
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
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

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

    private HotelMiniDTO ConvertHotelToDTO(Hotel hotel) {
        HotelMiniDTO hotelfinal = new HotelMiniDTO(hotel.getId(), hotel.getNombre(), hotel.getMunicipio(),
                hotel.getDireccion(), hotel.getImagen(), hotel.getPuntaje(), hotel.getValoracion().size(), null);
        return hotelfinal;
    }

    private HabitacionMiniDTO ConvertHabToDTO(Habitacion habitacion, Long dias) {
        HabitacionMiniDTO habitacionfinal = new HabitacionMiniDTO(habitacion.getNombre(),
                habitacion.getCapacidad(), habitacion.getPrecio() * dias);
        return habitacionfinal;
    }

    private boolean verificarDisponibilidad(Habitacion habitacion, int cantSoli) {
        Integer cantReserv = drRep.sumByHabitacionId(habitacion.getId(),
                List.of(EstadoReserva.REALIZADA, EstadoReserva.CONFIRMADA));
        if (cantReserv == null) {
            cantReserv = 0;
        }
        int disponible = habitacion.getCapacidad() - cantReserv;
        if (disponible >= cantSoli) {
            return true;
        }
        return false;
    }

}
