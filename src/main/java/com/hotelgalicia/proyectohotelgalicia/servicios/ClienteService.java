package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;

public interface ClienteService {

    List<Cliente> listAll();

    List<Cliente> listSorted(String val);

    Cliente getById(Long id);

    Cliente getByCorreo(String correo);

    Cliente agregar(Cliente user);

    Cliente modificar(Cliente user);

    Boolean DeshabilitarPorId(Long id);

}
