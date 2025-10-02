package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;

public interface HabitacionService {

    List<Habitacion> listHabitacionByHotelId(Long id);

    Habitacion agregar(Habitacion habitacion, MultipartFile file);

    Habitacion modificar(Habitacion habitacion, MultipartFile file);

    Habitacion cambiarEstado(EstadoHabitacionDTO estado);

    Habitacion DesabilitarPorId(Long id);

}
