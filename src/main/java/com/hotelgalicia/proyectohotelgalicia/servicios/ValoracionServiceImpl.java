package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.domain.ValoracionID;
import com.hotelgalicia.proyectohotelgalicia.dto.ValoracionDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.PermissionDeniedException;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ClienteRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.HotelRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;
import com.hotelgalicia.proyectohotelgalicia.repositorios.ValoracionRepository;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Autowired
    private UsuarioRepository uRep;

    @Autowired
    private ValoracionRepository vaRep;

    @Autowired
    private ClienteRepository cRep;

    @Autowired
    private HotelRepository hoRep;

    @Override
    public List<Valoracion> listAll() {
        return vaRep.findAll();
    }

    @Override
    public List<Valoracion> listByHotelId(Long id) {
        return vaRep.findByHotelId(id);
    }

    @Override
    public List<Valoracion> listByUserMail(String correo) {
        return vaRep.findByClienteCorreo(correo);
    }

    @Override
    public Boolean agregar(ValoracionDTO val) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Valoracion valor = new Valoracion();
        valor.setCliente(cRep.findByCorreoIgnoreCase(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario.")));
        valor.setHotel(hoRep.findById(val.getHotel()).orElseThrow(() -> new RuntimeException("Hotel no encontrado.")));
        valor.setPuntaje(val.getPuntaje());
        valor.setComentario(val.getComentario());
        try {
            vaRep.save(valor);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la valoración: " + e.getMessage());
        }
    }

    @Override
    public boolean borrarPorId(Long iduser, Long idhotel) {
        verificarpropiedad(iduser);
        ValoracionID id = new ValoracionID(iduser, idhotel);
        if (vaRep.existsById(id)) {
            vaRep.deleteById(id);
            return true;
        }
        return false;
    }

    private void verificarpropiedad(Long id) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = uRep.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Error: No se pudieron recuperar los datos del usuario."));

        switch (usuario.getRol()) {
            case ADMIN -> {
            }
            case USER -> {
                if (!id.equals(usuario.getId())) {
                    throw new PermissionDeniedException("No poseé permisos para realizar esta acción.");
                }
            }
            default -> throw new PermissionDeniedException("No está autorizado para realizar esta acción.");
        }
    }
}
