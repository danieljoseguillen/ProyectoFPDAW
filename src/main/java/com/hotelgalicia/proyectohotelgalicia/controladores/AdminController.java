package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.UsuarioDTO;
import com.hotelgalicia.proyectohotelgalicia.servicios.AdminService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ValoracionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired 
    private AdminService aServ;

    @Autowired
    private ClienteService cServ;

    @Autowired
    private HabitacionService haServ;

    @Autowired
    private ReservaService reServ;

    @Autowired
    private ValoracionService vaServ;

    @Autowired
    private ModelMapper modelMapper;

    private String formatBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));
    }

    // Retorna la id del usuario.
    private Long retornarId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return null;
        }
        return cServ.getByCorreo(authentication.getName()).getId();
    }

    // profile get
    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = aServ.getByCorreo(authentication.getName());
        model.addAttribute("usuario", usuario);
        return "admin/ProfileView";
    }

    // edit profile get
    @GetMapping("/editprofile")
    public String getedit(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("usuario",
                    modelMapper.map(aServ.getByCorreo(authentication.getName()), UsuarioDTO.class));
            return "admin/userEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/profile";
        }
    }

        // edit profile post
    @PostMapping("/editprofile/submit")
    public String postedit(@Valid UsuarioDTO usuario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("usuario", usuario);
            return "redirect:/admin/editprofile";
        }
        try {
            aServ.modificar(usuario);
            redirectAttributes.addFlashAttribute("message", "Datos actualizados con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/profile";
    }

    @GetMapping("/password")
    public String getPasswordChange(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("formulario", new ClaveDTO(null, null));
            return "admin/changePasswordView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/profile";
        }
    }

    @PostMapping("/password/submit")
    public String postPasswordChange(@Valid ClaveDTO formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            return "redirect:/admin/password";
        }
        try {
            aServ.cambiarContraseña(formulario);
            redirectAttributes.addFlashAttribute("message", "Contraseña actualizada con exito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/profile";
    }
}
