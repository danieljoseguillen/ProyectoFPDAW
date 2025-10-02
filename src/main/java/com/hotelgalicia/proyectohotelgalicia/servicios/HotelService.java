package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;

public interface HotelService {

    List<Hotel> listAllHotel();

    List<Hotel> listSortedHotel();

    List<Hotel> listHotelByCorpo();

    Hotel getById(Long id);

    Hotel agregar(Hotel hotel);

    Hotel modificar(Hotel hotel);

    Hotel DesabilitarPorId(Long id);

}
