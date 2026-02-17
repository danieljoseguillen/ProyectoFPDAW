package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.CorreoDTO;

public interface ClienteService {

    List<Cliente> listAll();

    List<Cliente> listByNameOrSurname(String val);

    Page<Cliente> listByCorreo(String correo, Pageable pageable);

    Cliente getById(Long id);

    Cliente getByCorreo(String correo);

    Cliente agregar(ClienteDTO cliente);

    Cliente modificar(ClienteDTO user);

    Cliente modificarCorreo(CorreoDTO usuario);

    Boolean cambiarContraseña(ClaveDTO dto);

}
