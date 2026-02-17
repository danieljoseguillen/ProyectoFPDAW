package com.hotelgalicia.proyectohotelgalicia.servicios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<Cliente> getSortedClientes (SortDTO formulario, Pageable pageable);

    Page<Empresa> getSortedEmpresa (SortDTO formulario, Pageable pageable);

    Boolean cambiarEstadoPorId(Long id);

    Cliente modificarCliente(ClienteDTOAdmin user, Long id);

    Empresa modificarEmpresa(EmpresaDTOAdmin user, Long id);

    Boolean cambiarContraseñaPorId(Long id, ClaveDTOAdmin dto);
}
