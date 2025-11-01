package com.hotelgalicia.proyectohotelgalicia.servicios;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.UsuarioDTO;

public interface AdminService {
    Usuario getById(Long id);

    Usuario getByCorreo(String correo);

    Usuario modificar(UsuarioDTO usuario);

    Boolean cambiarContraseña(ClaveDTO dto);

    //Parte administrativa
    public Boolean cambiarEstadoPorId(Long id, boolean estado);

    Cliente modificarCliente(ClienteDTO user, Long id);

    Empresa modificarEmpresa(EmpresaDTO user, Long id);

    Boolean cambiarContraseñaPorId(Long id, ClaveDTO dto);
}
