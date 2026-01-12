package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.SortDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.UsuarioDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;
import com.hotelgalicia.proyectohotelgalicia.security.UserDetailsImpl;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UsuarioRepository uRep;

    @Autowired
    private ClienteRepository cRep;

    @Autowired
    private EmpresaRepository eRep;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Usuario getById(Long id) {
        return uRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
    }

    @Override
    public Usuario getByCorreo(String correo) {
        return uRep.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
    }

    @Override
    public Usuario modificar(UsuarioDTO usuario) {
        Usuario base = uRep.findByCorreoIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        uRep.findByCorreo(usuario.getCorreo().trim().toLowerCase()).ifPresent(user -> {
            if (!user.getId().equals(base.getId())) {
                throw new RuntimeException("El correo ingresado ya está en uso.");
            }
        });
        if (!encoder.matches(usuario.getContraseña(), base.getContraseña())) {
            throw new BadCredentialsException("Contraseña incorrecta.");
        }
        base.setCorreo(usuario.getCorreo().trim());
                try {
            Usuario usuarioModificado = uRep.save(base);

            // Actualizar la autenticación en el contexto de seguridad con el nuevo correo
            UserDetailsImpl userDetails = UserDetailsImpl.build(usuarioModificado, "Administrador #"+usuarioModificado.getId());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return usuarioModificado;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Error al modificar el correo: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al modificar datos del usuario: " + e.getMessage());
        }
    }

    @Override
    public Boolean cambiarContraseña(ClaveDTO dto) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        if (!encoder.matches(dto.getClaveActual().trim(), usuario.getContraseña())) {
            throw new BadCredentialsException("Error: Contraseña incorrecta.");
        }

        if (encoder.matches(dto.getClaveNueva().trim(), usuario.getContraseña())) {
            throw new BadCredentialsException("Error: La contraseña nueva no puede ser igual a a la anterior.");
        }
        usuario.setContraseña(encoder.encode(dto.getClaveNueva().trim()));
        try {
            uRep.save(usuario);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Ocurrió un error al realizar la operación: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }

    // Parte administrativa
    // private Usuario retornarUser() {
    //     String correo = SecurityContextHolder.getContext().getAuthentication().getName();
    //     Usuario usuario = uRep.findByCorreoIgnoreCase(correo)
    //             .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
    //     return usuario;
    // }

    private void verificarAdmin() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        if (!usuario.getRol().equals(Roles.ADMIN)) {
            throw new PermissionDeniedException("No está autorizado para realizar esta acción.");
        }
    }

    @Override
    public List<Cliente> getSortedClientes(SortDTO formulario) {
        if (formulario.getSortname() == null)
            formulario.setSortname("");
        switch (formulario.getSorttype()) {
            case "NOMBRE" -> {
                return cRep.findByNombreContainingIgnoreCase(formulario.getSortname());
            }
            case "CORREO" -> {
                return cRep.findByCorreoContainingIgnoreCase(formulario.getSortname());
            }
            default -> {
                return cRep.findAll();
            }
        }
    }

    @Override
    public List<Empresa> getSortedEmpresa(SortDTO formulario) {
        if (formulario.getSortname() == null)
            formulario.setSortname("");
        switch (formulario.getSorttype()) {
            case "RAZON" -> {
                return eRep.findByRazonSocialContainingIgnoreCase(formulario.getSortname());
            }
            case "CORREO" -> {
                return eRep.findByCorreoContainingIgnoreCase(formulario.getSortname());
            }
            default -> {
                return eRep.findAll();
            }
        }
    }

    @Override
    public Boolean cambiarEstadoPorId(Long id, boolean estado) {
        Usuario objetivo = uRep.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Error: No se pudieron recuperar los datos del usuario a modificar."));
        // verifica que el usuario sea admin
        verificarAdmin();
        try {
            objetivo.setEstado(estado);
            uRep.save(objetivo);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Error al cambiar el estado del usuario: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }

    @Override
    public Cliente modificarCliente(ClienteDTOAdmin cliente, Long id) {
        Cliente base = cRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        verificarAdmin();
        uRep.findByCorreo(cliente.getCorreo().trim().toLowerCase()).ifPresent(user -> {
            if (!user.getId().equals(base.getId())) {
                throw new RuntimeException("El correo ingresado ya está en uso.");
            }
        });
        // if (!encoder.matches(cliente.getContraseña(), base.getContraseña())) {
        // throw new BadCredentialsException("Contraseña incorrecta.");
        // }
        base.setCorreo(cliente.getCorreo().trim());
        base.setNombre(cliente.getNombre().trim());
        base.setApellido(cliente.getApellido().trim());
        base.setTelefono(cliente.getTelefono().trim());
        try {
            return cRep.save(base);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Error al modificar datos del usuario: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al modificar datos del usuario: " + e.getMessage());
        }
    }

    @Override
    public Empresa modificarEmpresa(EmpresaDTOAdmin usuario, Long id) {
        Empresa base = eRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        verificarAdmin();
        uRep.findByCorreoIgnoreCase(usuario.getCorreo().trim().toLowerCase()).ifPresent(user -> {
            if (!user.getId().equals(base.getId())) {
                throw new RuntimeException("El correo ingresado ya está en uso.");
            }
        });

        // if (!encoder.matches(usuario.getContraseña().trim(), base.getContraseña())) {
        // throw new BadCredentialsException("Contraseña incorrecta.");
        // }

        base.setCorreo(usuario.getCorreo().trim());
        base.setRazonSocial(usuario.getRazon().trim());
        base.setCif(usuario.getCif().trim());

        try {
            return eRep.save(base);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Error al modificar datos del usuario: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al modificar datos del usuario: " + e.getMessage());
        }
    }

    @Override
    public Boolean cambiarContraseñaPorId(Long id, ClaveDTOAdmin dto) {
        Usuario usuario = uRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
        // verifica que el usuario sea admin
        verificarAdmin();
        if (encoder.matches(dto.getClaveNueva().trim(), usuario.getContraseña())) {
            throw new BadCredentialsException("Error: La contraseña nueva no puede ser igual a a la anterior.");
        }
        usuario.setContraseña(encoder.encode(dto.getClaveNueva().trim()));
        try {
            uRep.save(usuario);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Ocurrió un error al realizar la operación: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }
}
