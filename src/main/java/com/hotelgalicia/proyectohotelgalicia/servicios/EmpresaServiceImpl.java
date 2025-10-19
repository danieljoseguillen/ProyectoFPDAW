package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.excepciones.SaveFailedException;
import com.hotelgalicia.proyectohotelgalicia.modelos.Roles;
import com.hotelgalicia.proyectohotelgalicia.repositorios.EmpresaRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmpresaRepository eRep;

    @Autowired
    private UsuarioRepository uRep;

    @Override
    public List<Empresa> listAll() {
        return eRep.findAll();
    }

    @Override
    public List<Empresa> listByRazon(String val) {
        if (val == null)
            val = "";
        return eRep.findByRazonSocialContainingIgnoreCase(val);
    }

    @Override
    public List<Empresa> listByCorreo(String val) {
        if (val == null)
            val = "";
        return eRep.findByCorreoContainingIgnoreCase(val);
    }

    @Override
    public Empresa getById(Long id) {
        return eRep.findById(id).orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
    }

    @Override
    public Empresa getByCorreo(String correo) {
        return eRep.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
    }

    @Override
    public Empresa agregar(EmpresaDTO user) {
        if (uRep.findByCorreo(user.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ingresado ya está en uso.");
        }
        Empresa empresafinal = Empresa.builder()
                .correo(user.getCorreo().trim().toLowerCase())
                .contraseña(encoder.encode(user.getContraseña().trim()))
                .rol(Roles.USER)
                .estado(true)
                .razonSocial(user.getRazon().trim())
                .cif(user.getCif().trim())
                .build();
        try {
            return eRep.save(empresafinal);
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException("Error al agregar al usuario: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al agregar al usuario: " + e.getMessage());
        }
    }

    @Override
    public Empresa modificar(EmpresaDTO usuario, Long id) {
        Empresa base = eRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        verificarpropiedad(base);
        uRep.findByCorreo(usuario.getCorreo().trim().toLowerCase()).ifPresent(user -> {
            if (!user.getId().equals(base.getId())) {
                throw new RuntimeException("El correo ingresado ya está en uso.");
            }
        });

        if (!encoder.matches(usuario.getContraseña().trim(), base.getContraseña())) {
            throw new BadCredentialsException("Contraseña incorrecta.");
        }

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
    public Boolean cambiarEstadoPorId(Long id, boolean estado) {
        Empresa empresa = eRep.findById(id).orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
        // verifica que el usuario sea admin
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));
        if (!usuario.getRol().equals(Roles.ADMIN)) {
            throw new PermissionDeniedException("No está autorizado para realizar esta acción.");
        }

        try {
            empresa.setEstado(estado);
            eRep.save(empresa);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Error al cambiar el estado del usuario: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }

    @Override
    public Boolean cambiarContraseñaPorId(Long id, ClaveDTO dto) {
        Empresa empresa = eRep.findById(id).orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario"));
        verificarpropiedad(empresa);
        if (!encoder.matches(dto.getClaveActual().trim(), empresa.getContraseña())) {
            throw new BadCredentialsException("Error: Contraseña incorrecta.");
        }
        if (encoder.matches(dto.getClaveNueva().trim(), empresa.getContraseña())) {
            throw new BadCredentialsException("Error: La contraseña nueva no puede ser igual a a la anterior.");
        }
        empresa.setContraseña(encoder.encode(dto.getClaveNueva().trim()));
        try {
            eRep.save(empresa);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new SaveFailedException(
                    "Ocurrió un error al realizar la operación: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado  al realizar la operación:" + e.getMessage());
        }
    }

    private void verificarpropiedad(Empresa empresa) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));

        switch (usuario.getRol()) {
            case ADMIN -> {
            }
            case CORPORATION -> {
                if (!empresa.getId().equals(usuario.getId())) {
                    throw new PermissionDeniedException("No poseé permisos para realizar esta acción.");
                }
            }
            default -> throw new PermissionDeniedException("No está autorizado para realizar esta acción.");
        }
    }

}
