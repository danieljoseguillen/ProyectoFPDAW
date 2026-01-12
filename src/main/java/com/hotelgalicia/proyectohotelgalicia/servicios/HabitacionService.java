package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionListDTO;

public interface HabitacionService {

    List<HabitacionListDTO> listHabitacionByHotelId(Long id);

    List<HabitacionListDTO> listHabitacionByHotelIdDisponibles(Long id, LocalDate entrada, LocalDate salida);
    
    List<Habitacion> listHabitacionByHotelIdEmpresa(Long id);
    
    Habitacion getById(Long id);

    Habitacion agregar(HabitacionDTO habitacion, Long hotelId, MultipartFile file);

    Habitacion modificar(HabitacionDTO habitacion, Long habId, MultipartFile file);

    Habitacion cambiarEstado(Long id);

    Habitacion DesabilitarPorId(Long id);

}
