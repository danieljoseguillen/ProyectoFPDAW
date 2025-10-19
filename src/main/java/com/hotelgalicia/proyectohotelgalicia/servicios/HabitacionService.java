package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionListDTO;

public interface HabitacionService {

    List<HabitacionListDTO> listHabitacionByHotelId(Long id);
    
    List<Habitacion> listHabitacionByHotelIdEmpresa(Long id);
    
    Habitacion getById(Long id);

    Habitacion agregar(HabitacionDTO habitacion, Long hotelId, MultipartFile file);

    Habitacion modificar(HabitacionDTO habitacion, Long habId, MultipartFile file);

    Habitacion cambiarEstado(EstadoHabitacionDTO estado);

    Habitacion DesabilitarPorId(Long id);

}
