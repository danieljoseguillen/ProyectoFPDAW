package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.SimpleHotelSearchDTO;

public interface HotelService {

    List<Hotel> listAllHotel();

    List<HotelMiniDTO> listSortedHotel(HotelSearchDTO dto);

    List<HotelMiniDTO> listSortedHotel(SimpleHotelSearchDTO dto);

    List<Hotel> listHotelByCorpo(Long id);

    Hotel getById(Long id);

    Hotel agregar(HotelDTO hotel, MultipartFile file);

    Hotel modificar(HotelDTO hotel, Long hotelid, MultipartFile file);

    Hotel cambiarEstadoPorId(Long id);

    void verificarHotel(Hotel hotel);

    HotelMiniDTO ConvertHotelToDTO(Hotel hotel);

}
