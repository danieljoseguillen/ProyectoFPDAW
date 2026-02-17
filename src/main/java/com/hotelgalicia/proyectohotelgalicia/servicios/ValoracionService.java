package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.dto.ValoracionDTO;

public interface ValoracionService {
    List<Valoracion> listAll();

    Page<Valoracion> listByHotelId(Long id, Pageable pageable);

    Page<Valoracion> listByUserMail(String correo, Pageable pageable);

    Page<Valoracion> listByUserId(Long id, Pageable pageable);

    Valoracion getByIds(Long iduser, Long idhotel);

    Boolean agregar(ValoracionDTO val);

    boolean borrarPorId(Long iduser, Long idhotel);
}
