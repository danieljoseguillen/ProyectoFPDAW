package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionListDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoReserva;
import com.hotelgalicia.proyectohotelgalicia.repositorios.DetalleReservaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HabitacionRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;

@Service
public class HabitacionServiceImpl implements HabitacionService {

    @Autowired
    private HotelService hoServ;

    @Autowired
    private HotelRepository hoRep;

    @Autowired
    private HabitacionRepository haRep;

    @Autowired
    private DetalleReservaRepository drRep;

    @Autowired
    public FileStorageService fileserv;

    @Override
    public List<HabitacionListDTO> listHabitacionByHotelId(Long id) {
        return haRep.findByHotelIdAndEstado(id, EstadoHabitacion.DISPONIBLE)
                .stream()
                .map(hab -> {
                    HabitacionListDTO dto = new HabitacionListDTO();
                    dto.setId(hab.getId());
                    dto.setNombre(hab.getNombre());
                    dto.setDescripcion(hab.getDescripcion());
                    dto.setCapacidad(hab.getCapacidad());
                    dto.setPrecio(hab.getPrecio());
                    dto.setImagen(hab.getImagen());
                    return dto;
                })
                .toList();
    }

    @Override
    public List<HabitacionListDTO> listHabitacionByHotelIdDisponibles(Long id, LocalDate entrada, LocalDate salida) {
        Long dias = ChronoUnit.DAYS.between(entrada, salida);

        List<Habitacion> habitaciones = haRep.findByHotelIdAndEstado(id, EstadoHabitacion.DISPONIBLE);

        if (habitaciones.isEmpty())
            return new ArrayList<>();

        List<Long> idsHabitaciones = habitaciones.stream().map(Habitacion::getId).toList();

        Map<Long, Integer> mapaReservas = drRep.sumAllByHabitacionIds(
                idsHabitaciones,
                List.of(EstadoReserva.REALIZADA, EstadoReserva.CONFIRMADA),
                entrada,
                salida).stream().collect(Collectors.toMap(
                        obj -> (Long) obj[0],
                        obj -> ((Long) obj[1]).intValue()));
        return habitaciones.stream()
                .map(hab -> {
                    int ocupadas = mapaReservas.getOrDefault(hab.getId(), 0);
                    int libres = hab.getCantidad() - ocupadas;

                    if (libres <= 0)
                        return null;

                    return new HabitacionListDTO(
                            hab.getId(),
                            hab.getNombre(),
                            hab.getDescripcion(),
                            hab.getCapacidad(),
                            hab.getPrecio() * dias,
                            hab.getImagen(),
                            libres);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<Habitacion> listHabitacionByHotelIdEmpresa(Long id) {
        return haRep.findByHotelId(id);
    }

    @Override
    public Habitacion getById(Long id) {
        Habitacion habitacion = haRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Habitacion no encontrada"));
        return habitacion;
    }

    @Override
    public Habitacion agregar(HabitacionDTO habi, Long hotelId, MultipartFile file) {
        Hotel hotel = hoRep.findById(hotelId).orElseThrow(() -> new RuntimeException("Error: Hotel no encontrado"));
        hoServ.verificarHotel(hotel);
        Habitacion habFinal = new Habitacion(null, habi.getNombre(),
                habi.getDescripcion(), habi.getCantidad(), habi.getCapacidad(),
                habi.getPrecio(), null, EstadoHabitacion.DISPONIBLE, hotel);
        if (!file.isEmpty()) {
            String nombreImagen = fileserv.store(file, hotel.getNombre() + "_hab");
            habFinal.setImagen(nombreImagen);
        }
        try {
            return haRep.save(habFinal);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al agregar la habitacion: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al crear la habitacion: " + e.getMessage());
        }
    }

    @Override
    public Habitacion modificar(HabitacionDTO habi, Long habId, MultipartFile file) {
        Habitacion habFinal = haRep.findById(habId)
                .orElseThrow(() -> new RuntimeException("Habitacion no encontrada."));
        hoServ.verificarHotel(habFinal.getHotel());
        habFinal.setNombre(habi.getNombre());
        habFinal.setDescripcion(habi.getDescripcion());
        habFinal.setCantidad(habi.getCantidad());
        habFinal.setPrecio(habi.getPrecio());
        if (file != null && !file.isEmpty()) {
            try {
                String nombreImagen = fileserv.store(file, habFinal.getHotel().getNombre() + "_hab");
                if (habFinal.getImagen() != null) {
                    fileserv.delete(habFinal.getImagen());
                }
                habFinal.setImagen(nombreImagen);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la imagen: " + e.getMessage());
            }
        }
        try {
            return haRep.save(habFinal);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al guardar los cambios: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public Habitacion cambiarEstado(Long id) {
        Habitacion habitacion = haRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Habitacion no encontrada"));
        hoServ.verificarHotel(habitacion.getHotel());
        if (habitacion.getEstado() == EstadoHabitacion.DISPONIBLE) {
            habitacion.setEstado(EstadoHabitacion.INACTIVA);
        } else if (habitacion.getEstado() == EstadoHabitacion.INACTIVA) {
            habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
        }
        if (habitacion.getEstado() == EstadoHabitacion.ELIMINADA) {
            throw new RuntimeException("Error: La habitación está desactivada(Eliminada para futuras busquedas).");
        }
        try {
            return haRep.save(habitacion);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al guardar los cambios: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public Habitacion cambiarEstado(EstadoHabitacionDTO estado) {
        Habitacion habitacion = haRep.findById(estado.getId())
                .orElseThrow(() -> new RuntimeException("Error: Habitacion no encontrada"));
        hoServ.verificarHotel(habitacion.getHotel());
        habitacion.setEstado(estado.getEstado());
        try {
            return haRep.save(habitacion);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al guardar los cambios: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public Habitacion DesabilitarPorId(Long id) {
        Habitacion habitacion = haRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Habitacion no encontrada"));
        hoServ.verificarHotel(habitacion.getHotel());
        habitacion.setEstado(EstadoHabitacion.ELIMINADA);
        try {
            return haRep.save(habitacion);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al guardar los cambios: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar los cambios: " + e.getMessage());
        }
    }

}
