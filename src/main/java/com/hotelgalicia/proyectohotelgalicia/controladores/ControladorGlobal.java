package com.hotelgalicia.proyectohotelgalicia.controladores;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.security.UserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", "El tamaño de la imagen excede el límite permitido");
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            // Extraemos solo la ruta para evitar problemas con dominios externos
            return "redirect:" + referer;
        }
        return "redirect:/";
    }
}
