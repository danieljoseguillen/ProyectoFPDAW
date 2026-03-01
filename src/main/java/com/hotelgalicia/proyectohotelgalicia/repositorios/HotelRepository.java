package com.hotelgalicia.proyectohotelgalicia.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

        List<Hotel> findByEmpresaId(Long id);

        List<Hotel> findByEstado(Boolean estado);

        @Query("SELECT DISTINCT h FROM Hotel h " +
                        "LEFT JOIN FETCH h.valoracion v " +
                        "LEFT JOIN FETCH v.cliente c " +
                        "WHERE h.id = :id")
        Optional<Hotel> findByIdPage(Long id);

        @Query("SELECT DISTINCT h FROM Hotel h LEFT JOIN FETCH h.valoracion " +
                        "WHERE UPPER(h.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) " +
                        "AND h.municipio = :municipio " +
                        "AND UPPER(h.direccion) LIKE UPPER(CONCAT('%', :direccion, '%')) " +
                        "AND h.estado = :estado")
        List<Hotel> findByNombreContainingIgnoreCaseAndMunicipioAndDireccionContainingIgnoreCaseAndEstado(
                        @Param("nombre") String nombre,
                        @Param("municipio") Municipios municipio,
                        @Param("direccion") String direccion,
                        @Param("estado") boolean estado);

        @Query("SELECT DISTINCT h FROM Hotel h LEFT JOIN FETCH h.valoracion " +
                        "WHERE UPPER(h.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) " +
                        "AND UPPER(h.direccion) LIKE UPPER(CONCAT('%', :direccion, '%')) " +
                        "AND h.estado = :estado")
        List<Hotel> findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCaseAndEstado(
                        @Param("nombre") String nombre,
                        @Param("direccion") String direccion,
                        @Param("estado") boolean estado);

        @Query("SELECT DISTINCT h FROM Hotel h LEFT JOIN FETCH h.valoracion " +
                        "WHERE UPPER(h.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) " +
                        "AND h.municipio = :municipio " +
                        "AND UPPER(h.direccion) LIKE UPPER(CONCAT('%', :direccion, '%'))")
        List<Hotel> findByNombreContainingIgnoreCaseAndMunicipioAndDireccionContainingIgnoreCase(
                        @Param("nombre") String nombre,
                        @Param("municipio") Municipios municipio,
                        @Param("direccion") String direccion);

        @Query("SELECT DISTINCT h FROM Hotel h LEFT JOIN FETCH h.valoracion " +
                        "WHERE UPPER(h.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) " +
                        "AND UPPER(h.direccion) LIKE UPPER(CONCAT('%', :direccion, '%'))")
        List<Hotel> findByNombreContainingIgnoreCaseAndDireccionContainingIgnoreCase(
                        @Param("nombre") String nombre,
                        @Param("direccion") String direccion);

}
