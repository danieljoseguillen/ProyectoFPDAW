package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.dto.ValoracionDTO;

public interface ValoracionService {
    List<Valoracion> listAll();

    List<Valoracion> listByHotelId(Long id);

    List<Valoracion> listByUserMail(String correo);

    Valoracion getByIds(Long iduser, Long idhotel);

    Boolean agregar(ValoracionDTO val);

    boolean borrarPorId(Long iduser, Long idhotel);
}
