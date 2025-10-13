package com.hotelgalicia.proyectohotelgalicia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.repositorios.UsuarioRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findFullUsuarioByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado!"));
        String nombre = "";
        switch (usuario.getRol()) {
            case USER -> {
                nombre = ((Cliente)usuario).getNombre() + " " + ((Cliente)usuario).getApellido();
            }
            case CORPORATION -> {
                nombre = ((Empresa)usuario).getRazonSocial();
            }
            case ADMIN -> {
                nombre = "Administrador #" + usuario.getId();
            }
            default -> {
                throw new RuntimeException("Error en los datos del usuario.");
            }
        }
        return UserDetailsImpl.build(usuario, nombre);
    }
}
