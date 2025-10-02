package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;

public interface EmpresaService {

    List<Empresa> listAll();

    List<Empresa> listSorted(String val);

    Empresa getById(Long id);

    Empresa getByCorreo(String correo);

    Empresa agregar(Empresa user);

    Empresa modificar(Empresa user);

    Boolean DeshabilitarPorId(Long id);
}
