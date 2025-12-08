package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.CorreoDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;

public interface EmpresaService {

    List<Empresa> listAll();

    List<Empresa> listByRazon(String val);

    List<Empresa> listByCorreo(String val);

    Empresa getById(Long id);

    Empresa getByCorreo(String correo);

    Empresa agregar(EmpresaDTO user);

    Empresa modificar(CorreoDTO user);

    Boolean cambiarContraseñaPorId(ClaveDTO dto);
}
