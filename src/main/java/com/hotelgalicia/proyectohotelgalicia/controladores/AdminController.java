package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTOAdmin;
import com.hotelgalicia.proyectohotelgalicia.dto.DetalleReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionListDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.SortDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.UsuarioDTO;
import com.hotelgalicia.proyectohotelgalicia.servicios.AdminService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ValoracionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@SessionAttributes({ "userid", "reservaId", "hotelid" })
public class AdminController {

    @Autowired
    private AdminService aServ;

    @Autowired
    private ClienteService cServ;

    @Autowired
    private HotelService hoServ;

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

    private void verificarsessionvalue(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("Ocurrió un error en la carga de los datos.");
        }
    }

    // Verifica tipo de recurso
    private void verificarTipo(String type) {
        if (!type.equals("users") && !type.equals("hotels")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dirección no valida.");
        }
    }

    // Retorna la id del usuario.
    // private Usuario retornarUser() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     if (authentication == null || !authentication.isAuthenticated()
    //             || "anonymousUser".equals(authentication.getName())) {
    //         return null;
    //     }
    //     return cServ.getByCorreo(authentication.getName());
    // }

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

    // zona lista de usuario

    @GetMapping("/users")
    public String getClientList(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("listado", cServ.listAll());
            model.addAttribute("sortform", new SortDTO());
            return "userListView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
    }

    @PostMapping("users/sort/name")
    public String postIClientSorted(SortDTO sortform, Model model) {
        model.addAttribute("listado", aServ.getSortedClientes(sortform));
        model.addAttribute("sortform", sortform);
        return "admin/usuariolistView";
    }

    // zona usuario
    @GetMapping("/users/{id}")
    public String getUserData(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("usuario", cServ.getById(id));
            model.addAttribute("userid", id);
            return "admin/usuarioView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/users/{id}/edit")
    public String geteditUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @ModelAttribute("userid") Long userid) {
        try {
            verificarsessionvalue(userid);
            model.addAttribute("cliente",
                    modelMapper.map(cServ.getById(id), ClienteDTO.class));

            return "admin/userEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/" + id;
        }
    }

    @PostMapping("/users/edit/submit")
    public String posteditUser(@Valid ClienteDTOAdmin cliente, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, @ModelAttribute("userid") Long userid, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("cliente", cliente);
        } else {
            try {
                verificarsessionvalue(userid);
                aServ.modificarCliente(cliente, userid);
                status.setComplete();
                redirectAttributes.addFlashAttribute("message", "Datos actualizados con éxito.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/admin/users/" + userid;
    }

    @GetMapping("/users/{id}/password")
    public String getUserPasswordChange(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @ModelAttribute("userid") Long userid) {
        try {
            verificarsessionvalue(userid);
            model.addAttribute("formulario", new ClaveDTOAdmin(null));
            return "admin/changePasswordView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/" + id;
        }
    }

    @PostMapping("/users/password/submit")
    public String postUserPasswordChange(@Valid ClaveDTOAdmin formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, @ModelAttribute("userid") Long userid, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            return "redirect:/admin/users/" + userid + "password";
        }
        try {
            verificarsessionvalue(userid);
            aServ.cambiarContraseñaPorId(userid, formulario);
            redirectAttributes.addFlashAttribute("message", "Contraseña actualizada con exito.");
            status.setComplete();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users/" + userid;
    }

    // Zona hotel

    // Muestra reseñas de hotel o usuario
    @GetMapping("/{type}/{id}/reviews")
    public String getUserReviewListUser(@PathVariable String type, @PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            verificarTipo(type);
            if (type.equals("users")) {
                model.addAttribute("reviews", vaServ.listByUserId(id));
                model.addAttribute("usuario", cServ.getById(id));
            } else if (type.equals("hotels")) {
                model.addAttribute("reviews", vaServ.listByHotelId(id));
                model.addAttribute("hotel", hoServ.getById(id));
            }
            return "admin/reviewListView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            switch (type) {
                case "users" -> {
                    return "redirect:/admin/users/" + id;
                }
                case "hotels" -> {
                    return "redirect:/hotel/" + id;
                }
                default -> {
                    return "redirect:/index";
                }
            }
        }
    }

    // Borrar reviews de usuario u hotel
    @PostMapping("/{type}/{id}/reviews/delete")
    public String postReviewDeleteUser(@PathVariable String type, @PathVariable Long id, @RequestParam Long idhotel,
            @RequestParam Long iduser, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            verificarTipo(type);
            vaServ.borrarPorId(iduser, idhotel);
            redirectAttributes.addFlashAttribute("message", "Reseña eliminada con exito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/"+type+"/" + id + "/reserves";
    }

    // Reservas de usuario y hotel
    @GetMapping("/{type}/{typeid}/reserves")
    public String getReserveList(@PathVariable String type, @PathVariable Long typeid, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            verificarTipo(type);
            if (type.equals("users")) {
                model.addAttribute("reserves", reServ.listByCliente(typeid));
            } else if (type.equals("hotels")) {
                model.addAttribute("reserves", reServ.listByHotel(typeid));
            }
            model.addAttribute("type", type);
            model.addAttribute("typeid", typeid);
            return "admin/usuarioReserveView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            switch (type) {
                case "users" -> {
                    return "redirect:admin/users/" + typeid;
                }
                case "hotels" -> {
                    return "redirect:/hotel/" + typeid;
                }
                default -> {
                    return "redirect:/index";
                }
            }
        }
    }

    // zona reserva
    @GetMapping("/{type}/{typeid}/reserves/{idres}")
    public String getReserveDetails(@PathVariable String type, @PathVariable Long typeid, @PathVariable Long idres,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            verificarTipo(type);
            Reserva reserva = reServ.getById(idres);
            model.addAttribute("hotel", hoServ.ConvertHotelToDTO(reserva.getHotel()));
            model.addAttribute("reserva", reserva);
            model.addAttribute("detalles", reserva.getHabitaciones());
            model.addAttribute("type", type);
            model.addAttribute("typeid", typeid);
            return "admin/reserveDetailsView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/" + type + "/" + typeid + "/reserves";
        }
    }

    @PostMapping("/{type}/{typeid}/reserves/{idres}/status")
    public String postReserveStatusChange(@Valid @ModelAttribute("estadoReserva") EstadoReservaDTO dto,
            BindingResult bindingResult, @PathVariable String type, @PathVariable Long typeid, @PathVariable Long idres,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
        } else {
            try {
                reServ.cambiarEstado(dto);
                redirectAttributes.addFlashAttribute("message", "Estado cambiado con exito.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/admin/" + type + "/" + typeid + "/reserves/" + idres;
    }

    @GetMapping("/{type}/{typeid}/reserves/{idres}/edit")
    public String getEditReserve(@PathVariable String type, @PathVariable Long typeid, @PathVariable Long idres,
            Model model, RedirectAttributes redirectAttributes,
            Principal principal, SessionStatus status) {
        try {
            // Reserva, Hotel y habitaciones
            Reserva reserva = reServ.getById(idres);
            Hotel hotel = reserva.getHotel();
            List<HabitacionListDTO> habitaciones = haServ.listHabitacionByHotelId(hotel.getId());
            // Valida reserva
            reServ.verificarReserva(reserva);

            // Crea el DTO
            // ReservaDTO reservaDTO = modelMapper.map(reserva, ReservaDTO.class);
            ReservaDTO reservaDTO = new ReservaDTO(reserva.getFechaInicio(), reserva.getFechaFin(),
                    reserva.getPersonas(), null);
            List<DetalleReservaDTO> detalles = habitaciones.stream()
                    .map(h -> new DetalleReservaDTO(h.getId(),
                            reserva.getHabitaciones().stream()
                                    .filter(r -> Objects.equals(r.getHabitacion().getId(), h.getId()))
                                    .map(n -> n.getCantidad()).findFirst().orElse(0)))
                    .toList();
            reservaDTO.setHabitaciones(detalles);
            model.addAttribute("reservaId", reserva.getId());
            model.addAttribute("reserva", reservaDTO);

            // agrega hotel y habitaciones
            model.addAttribute("hotel", hotel);
            model.addAttribute("habitaciones", habitaciones);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/" + type + "/" + typeid + "/reserves/" + idres;
        }
        return "admin/reservaEditView";
    }

    @PostMapping("/{type}/{typeid}/reserves/{idres}/edit/submit")
    public String postEditReserve(@Valid @ModelAttribute("reserva") ReservaDTO reserva,
            BindingResult bindingResult, @ModelAttribute("reservaId") Long reId,
            Model model, @PathVariable String type, @PathVariable Long typeid, @PathVariable Long idres,
            RedirectAttributes redirectAttributes, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("reserva", reserva);
        } else {
            try {
                reServ.verificarCantidad(reserva);
                reServ.modificar(reserva, reId);
                status.setComplete();
                redirectAttributes.addFlashAttribute("message", "Reserva modificada con exito.");
                return "redirect:/admin/" + type + "/" + typeid + "/reserves/" + idres;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }

        return "redirect:/admin/" + type + "/" + typeid + "/reserves/" + idres + "/edit";
    }

    // editar hotel get
    @GetMapping("/hotels/edit/{id}")
    public String getEditHotel(Model model,
            RedirectAttributes redirectAttributes, @PathVariable Long id, SessionStatus status) {
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);
            status.setComplete();
            model.addAttribute("hotelId", id);
            model.addAttribute("formulario", dto);
            return "empresa/hotelEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/hotel/" + id;
        }
    }

    // editar hotel post
    @PostMapping("/hotels/edit/submit")
    public String postEditHotel(@Valid @ModelAttribute("formulario") HotelDTO dto, BindingResult bindingResult,
            Model model, @RequestParam MultipartFile file, @ModelAttribute("hotelId") Long id,
            RedirectAttributes redirectAttributes, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("formulario", dto);
            return "redirect:/admin/hotels/edit/" + id;
        } else {
            try {
                hoServ.modificar(dto, id, file);
                redirectAttributes.addFlashAttribute("message", "Hotel modificado con éxito.");
                status.setComplete();
                return "redirect:/hotel/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/admin/hotels/edit/" + id;
            }
        }
    }

    // editar estado habitacion post
    @GetMapping("/hotels/{id}/status")
    public String getEditHotStatus(EstadoHabitacionDTO dto,
            BindingResult bindingResult, @PathVariable Long id,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
        } else {
            try {
                hoServ.cambiarEstadoPorId(id);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/hotel/" + id;
    }

    // editar habitacion get
    @GetMapping("/hotels/{id}/habitacion/edit/{habId}")
    public String getEditHab(Model model, @PathVariable Long id, @PathVariable Long habId,
            RedirectAttributes redirectAttributes, SessionStatus status) {
        try {
            Habitacion habi = haServ.getById(habId);
            hoServ.verificarHotel(habi.getHotel());
            status.setComplete();
            HabitacionDTO dto = modelMapper.map(habi, HabitacionDTO.class);
            model.addAttribute("formulario", dto);
            model.addAttribute("habitacionId", habId);
            return "empresa/habitacionEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/hotel/" + id;
        }
    }

    // editar habitacion post
    @PostMapping("/hotels/{id}/habitacion/edit/submit")
    public String postEditHabSubmit(@Valid @ModelAttribute("formulario") HabitacionDTO dto, BindingResult bindingResult,
            @PathVariable Long id, Model model, @RequestParam MultipartFile file,
            @ModelAttribute("habitacionId") Long habId,
            RedirectAttributes redirectAttributes, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("formulario", dto);
            return "redirect:/admin/hotels/" + id + "/habitacion/edit/" + habId;
        } else {
            try {
                haServ.modificar(dto, habId, file);
                status.setComplete();
                redirectAttributes.addFlashAttribute("message", "Habitación modificada con éxito.");
                return "redirect:/hotel/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/admin/hotels/" + id + "/habitacion/edit/" + habId;
            }
        }
    }

    /*
     * Por hacer:
     * editar reservas GET Y POST (Revisar)
     * hoteles editar desactivar GET Y POST (Revisar)
     * Habitaciones GET Y POST (revisar)
     */

}
