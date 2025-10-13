package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByEmpresaId(Long id);

    List<Hotel> findByEstado(Boolean estado);

    List<Hotel> findByNombreContainingIgnoreCaseAndMunicipioAndDireccionContainingIgnoreCaseAndEstado(
            String nombre, Municipios municipio, String direccion, boolean estado);

    List<Hotel> findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCaseAndEstado(
            String nombre, String direccion, boolean estado);

}
