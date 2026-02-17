package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.CorreoDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;

public interface EmpresaService {

    List<Empresa> listAll();

    Page<Empresa> listByRazon(String val, Pageable pageable);

    Page<Empresa> listByCorreo(String val, Pageable pageable);

    Empresa getById(Long id);

    Empresa getByCorreo(String correo);

    Empresa agregar(EmpresaDTO user);

    Empresa modificarCorreo(CorreoDTO user);

    Boolean cambiarContraseñaPorId(ClaveDTO dto);
}
