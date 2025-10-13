package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;

public interface ClienteService {

    List<Cliente> listAll();

    List<Cliente> listByNameOrSurname(String val);

    List<Cliente> listByCorreo(String correo);

    Cliente getById(Long id);

    Cliente getByCorreo(String correo);

    Cliente agregar(ClienteDTO cliente);

    Cliente modificar(ClienteDTO user, Long id);

    Boolean cambiarEstadoPorId(Long id, boolean estado);

    Boolean cambiarContraseñaPorId(Long id, ClaveDTO dto);

}
