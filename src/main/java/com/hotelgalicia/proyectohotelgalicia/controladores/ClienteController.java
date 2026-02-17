package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.security.Principal;
import java.time.temporal.ChronoUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Cliente;
import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Valoracion;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.CorreoDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaListDTO;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ValoracionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
@SessionAttributes({ "reservaId" })
public class ClienteController {

    @Autowired
    private ClienteService cServ;

    @Autowired
    private ReservaService reServ;

    @Autowired
    private ValoracionService vaServ;

    @Autowired
    private ModelMapper modelMapper;

    // Retorna la id del usuario.
    private Long retornarId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return null;
        }
        System.out.println(cServ.getByCorreo(authentication.getName()).getId());
        return cServ.getByCorreo(authentication.getName()).getId();
    }

    // profile get
    @GetMapping("/profile")
    public String getProfile(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Cliente cli = cServ.getByCorreo(authentication.getName());
            model.addAttribute("cliente", cli);
            return "cliente/userProfileView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
    }

    // edit profile get
    @GetMapping("/editprofile")
    public String getedit(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("cliente",
                    modelMapper.map(cServ.getByCorreo(authentication.getName()), ClienteDTO.class));
            return "cliente/userEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/profile";
        }
    }

    // edit profile post
    @PostMapping("/editprofile/submit")
    public String postedit(@Valid @ModelAttribute("cliente") ClienteDTO cliente, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // redirectAttributes.addFlashAttribute("error",
            // formatBindingErrors(bindingResult));
            model.addAttribute("cliente", cliente);
            return "cliente/userEditView";
        } else {
            try {
                cServ.modificar(cliente);
                redirectAttributes.addFlashAttribute("message", "Datos actualizados con éxito.");
                return "redirect:/user/profile";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("cliente", cliente);
        return "cliente/userEditView";
    }

    @GetMapping("/editmail")
    public String geteditmail(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("correo",
                    modelMapper.map(cServ.getByCorreo(authentication.getName()), CorreoDTO.class));
            return "cliente/changeMailView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
    }

    // edit profile post
    @PostMapping("/editmail/submit")
    public String posteditmail(@Valid @ModelAttribute("correo") CorreoDTO correo, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cliente/changeMailView";
        }
        try {
            cServ.modificarCorreo(correo);
            redirectAttributes.addFlashAttribute("message", "Correo actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/password")
    public String getPasswordChange(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("formulario", new ClaveDTO(null, null, null));
            return "cliente/changePasswordView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/profile";
        }
    }

    @PostMapping("/password/submit")
    public String postPasswordChange(@Valid @ModelAttribute("formulario") ClaveDTO formulario,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cliente/changePasswordView";
        } else {
            try {
                cServ.cambiarContraseña(formulario);
                redirectAttributes.addFlashAttribute("message", "Contraseña actualizada con exito.");
                return "redirect:/user/profile";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("formulario", formulario);
        return "cliente/changePasswordView";
    }

    @GetMapping("/valorations")
    public String getValorationUser(Model model, Principal principal, @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {
        Pageable pageable = PageRequest.of(page, 10);
        try {
            Page<Valoracion> listado = vaServ.listByUserMail(principal.getName(), pageable);
            model.addAttribute("valoraciones", listado);
            model.addAttribute("currentPage", listado.getNumber());
            model.addAttribute("totalPages", listado.getTotalPages());
            model.addAttribute("totalItems", listado.getTotalElements());
            return "cliente/valoraView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/index";
    }

    @PostMapping("/valoraciones/delete")
    public String postValorationDelete(@RequestParam(required = true) Long id, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            if (vaServ.borrarPorId(retornarId(), id)) {
                redirectAttributes.addFlashAttribute("message", "Reseña eliminada con exito.");
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "No se encontró la reseña o no tienes permiso para eliminarla.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/valorations";
    }

    @GetMapping("/reserves")
    public String getReserves(Model model, @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {
        Pageable pageable = PageRequest.of(page, 6);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Crea las reservas como reservalistdto
            Page<ReservaListDTO> reservas = reServ
                    .listarReservasCliente(cServ.getByCorreo(authentication.getName()).getId(), pageable);
            model.addAttribute("reservas", reservas);
            model.addAttribute("currentPage", reservas.getNumber());
            model.addAttribute("totalPages", reservas.getTotalPages());
            model.addAttribute("totalItems", reservas.getTotalElements());
            return "reserve/reserveListView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/index";
    }

    @GetMapping("/reserves/{id}")
    public String getReserveDetails(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Reserva reserva = reServ.getById(id);
            reServ.verificarReserva(reserva);
            model.addAttribute("reserva", reserva);
            model.addAttribute("detalles", reserva.getHabitaciones());
            int totalHabis = reserva.getHabitaciones().stream()
                    .mapToInt(DetalleReserva::getCantidad)
                    .sum();
            model.addAttribute("totalHabis", totalHabis);
            int totalprice = reServ.calcularPrecioTotal(reserva).intValue();
            model.addAttribute("totalprice", totalprice);
            model.addAttribute("dias", ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin()));
            return "reserve/reserveDetailsView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reserve/reserves";
        }
    }

    @PostMapping("/reserves/cancel")
    public String postCancelReserve(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes,
            SessionStatus status) {
        try {
            reServ.cancelarPorId(id);
            redirectAttributes.addFlashAttribute("message", "Reserva cancelada con exito.");
            status.setComplete();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/reserves";
    }
}
