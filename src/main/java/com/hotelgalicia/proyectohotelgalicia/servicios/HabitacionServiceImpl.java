package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.EstadoHabitacion;
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
    public fileStorageService fileserv;

    @Override
    public List<Habitacion> listHabitacionByHotelId(Long id) {
        return haRep.findByHotelIdAndEstado(id, EstadoHabitacion.DISPONIBLE);
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
        Habitacion habFinal = new Habitacion(null, habi.getNombre(),
                habi.getDescripcion(), habi.getCapacidad(),
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
        habFinal.setCapacidad(habi.getCapacidad());
        habFinal.setPrecio(habi.getPrecio());
        if (file != null && !file.isEmpty()) {
            try {
                if (habFinal.getImagen() != null) {
                    fileserv.delete(habFinal.getImagen());
                }
                String nombreImagen = fileserv.store(file, habFinal.getHotel().getNombre() + "_hab");
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
