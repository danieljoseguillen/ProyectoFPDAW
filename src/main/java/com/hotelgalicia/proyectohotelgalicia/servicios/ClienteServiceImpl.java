package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.CorreoDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;
import com.hotelgalicia.proyectohotelgalicia.security.UserDetailsImpl;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ClienteRepository cRep;

    @Autowired
    private UsuarioRepository uRep;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Cliente> listAll() {
        return cRep.findAll();
    }

    @Override
    public List<Cliente> listByNameOrSurname(String val) {
        if (val == null)
            val = "";
        return cRep.listByNameOrSurname(val);
    }

    @Override
    public List<Cliente> listByCorreo(String val) {
        if (val == null)
            val = "";
        return cRep.findByCorreoContainingIgnoreCase(val);
    }

    @Override
    public Cliente getById(Long id) {
        return cRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
    }

    @Override
    public Cliente getByCorreo(String correo) {
        return cRep.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
    }

    @Override
    public Cliente agregar(ClienteDTO cliente) {
        if (uRep.findByCorreo(cliente.getCorreo()).isPresent()) {
            throw new RuntimeException("Error: El correo ingresado ya está en uso.");
        }
        Cliente clientfinal = Cliente.builder()
                .correo(cliente.getCorreo().trim().toLowerCase())
                .contraseña(encoder.encode(cliente.getContraseña().trim()))
                .rol(Roles.USER)
                .estado(true)
                .nombre(cliente.getNombre().trim())
                .apellido(cliente.getApellido().trim())
                .telefono(cliente.getTelefono().trim())
                .build();
        try {
            return cRep.save(clientfinal);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al agregar al usuario: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al agregar al usuario: " + e.getMessage());
        }
    }

    @Override
    public Cliente modificar(ClienteDTO cliente) {
        Cliente base = retornarCliente();
        verificarpropiedad(base);

        if (!encoder.matches(cliente.getContraseña(), base.getContraseña())) {
            throw new BadCredentialsException("Contraseña incorrecta.");
        }
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
    public Cliente modificarCorreo(CorreoDTO usuario) {
        Cliente base = retornarCliente();
        verificarpropiedad(base);
        if (usuario.getCorreo().equals(base.getCorreo())) {
            throw new RuntimeException("Debe ingresar un correo diferente al actual.");
        }
        uRep.findByCorreoIgnoreCase(usuario.getCorreo().trim().toLowerCase()).ifPresent(user -> {
            if (!user.getId().equals(base.getId())) {
                throw new RuntimeException("El correo ingresado ya está en uso.");
            }
        });

        if (!encoder.matches(usuario.getContraseña().trim(), base.getContraseña())) {
            throw new BadCredentialsException("Contraseña incorrecta.");
        }

        base.setCorreo(usuario.getCorreo().trim());

        try {
            Cliente usuarioModificado = cRep.save(base);

            // Actualizar la autenticación en el contexto de seguridad con el nuevo correo
            UserDetailsImpl userDetails = UserDetailsImpl.build(usuarioModificado,
                    usuarioModificado.getNombre() + " " + usuarioModificado.getApellido());
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
        Cliente cliente = retornarCliente();
        if (!encoder.matches(dto.getClaveActual().trim(), cliente.getContraseña())) {
            throw new BadCredentialsException("Error: Contraseña incorrecta.");
        }

        if (encoder.matches(dto.getClaveNueva().trim(), cliente.getContraseña())) {
            throw new BadCredentialsException("Error: La contraseña nueva no puede ser igual a a la anterior.");
        }
        if (!dto.getClaveNueva().equals(dto.getClaveRepetir())) {
            throw new BadCredentialsException("Error: Las contraseñas no son iguales.");
        }
        cliente.setContraseña(encoder.encode(dto.getClaveNueva().trim()));
        try {
            cRep.save(cliente);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Ocurrió un error al realizar la operación: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }

    private Cliente retornarCliente() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Cliente cliente = cRep.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        return cliente;
    }

    private void verificarpropiedad(Cliente cliente) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));

        switch (usuario.getRol()) {
            case ADMIN -> {
            }
            case USER -> {
                if (!cliente.getId().equals(usuario.getId())) {
                    throw new PermissionDeniedException("No poseé permisos para realizar esta acción.");
                }
            }
            default -> throw new PermissionDeniedException("No está autorizado para realizar esta acción.");
        }
    }
}
