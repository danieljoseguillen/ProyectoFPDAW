package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.SortDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.UsuarioDTO;

public interface AdminService {
    Usuario getById(Long id);

    Usuario getByCorreo(String correo);

    Usuario modificar(UsuarioDTO usuario);

    Boolean cambiarContraseña(ClaveDTO dto);

    //Parte administrativa

    List<Cliente> getSortedClientes (SortDTO formulario);

    List<Empresa> getSortedEmpresa (SortDTO formulario);

    Boolean cambiarEstadoPorId(Long id);

    Cliente modificarCliente(ClienteDTOAdmin user, Long id);

    Empresa modificarEmpresa(EmpresaDTOAdmin user, Long id);

    Boolean cambiarContraseñaPorId(Long id, ClaveDTOAdmin dto);
}
