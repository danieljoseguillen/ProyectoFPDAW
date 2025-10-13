package com.hotelgalicia.proyectohotelgalicia.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchResultDTO {

    private Double precioMin;
    private Double precioMax;

    private int totalHoteles;
    
    private List<HotelMiniDTO> hoteles;
}
