package com.hotelgalicia.proyectohotelgalicia.controladores;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hotelgalicia.proyectohotelgalicia.security.UserDetailsImpl;

@ControllerAdvice("com.hotelgalicia.proyectohotelgalicia.controladores")
public class ControladorGlobal {

    @ModelAttribute
    public void addUserInfoToModel(Model model) {
        // Muestra el nombre del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            model.addAttribute("usuarioactual", userDetails.getName());
        }
    }
}
