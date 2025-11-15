package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoHabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/enterprise")
@SessionAttributes({ "hotelId", "habitacionId" })
public class CorpoController {

    @Autowired
    private EmpresaService eServ;

    @Autowired
    private HotelService hoServ;

    @Autowired
    private HabitacionService haServ;

    @Autowired
    private ReservaService reServ;

    @Autowired
    private ModelMapper modelMapper;

    // Retorna la id del usuario.
    // private Long retornarId() {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // if (authentication == null || !authentication.isAuthenticated() ||
    // "anonymousUser".equals(authentication.getName())) {
    // return null;
    // }
    // return eServ.getByCorreo(authentication.getName()).getId();
    // }

    private String formatBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));
    }

    // profile get
    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Empresa emp = eServ.getByCorreo(authentication.getName());
        model.addAttribute("empresa", emp);
        List<Hotel> hoteles = hoServ.listHotelByCorpo(emp.getId());
        model.addAttribute("hoteles", hoteles);
        return "empresa/empresaProfileView";
    }

    // edit profile get
    @GetMapping("/editprofile")
    public String getedit(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("empresa",
                    modelMapper.map(eServ.getByCorreo(authentication.getName()), EmpresaDTO.class));
            return "empresa/empresaEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
    }

    // edit profile post
    @PostMapping("/editprofile/submit")
    public String postedit(@Valid EmpresaDTO empresa, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("empresa", empresa);
            return "redirect:/enterprise/editprofile";
        }
        try {
            eServ.modificar(empresa);
            redirectAttributes.addFlashAttribute("message", "Datos actualizados con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enterprise/profile";
    }

    @GetMapping("/password")
    public String getPasswordChange(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("formulario", new ClaveDTO(null, null));
            return "empresa/changePasswordView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
    }

    @PostMapping("/password/submit")
    public String postPasswordChange(@Valid ClaveDTO formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            return "redirect:/enterprise/password";
        }
        try {
            eServ.cambiarContraseñaPorId(formulario);
            redirectAttributes.addFlashAttribute("message", "Contraseña actualizada con exito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enterprise/profile";
    }

    // ver hotel get
    @GetMapping("/hotels/{id}")
    public String getHotel(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            SessionStatus status) {
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            model.addAttribute("hotel", hotel);
            model.addAttribute("hotelId", id);
            model.addAttribute("habitaciones", hotel.getHabitaciones());
            model.addAttribute("valoraciones", hotel.getValoracion());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
        return "empresa/hotelEnterpriseView";
    }

    // Ver reservas de hotel
    @GetMapping("/hotels/{id}/reserves")
    public String getHotelReserves(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            model.addAttribute("listaReservas", reServ.listByHotel(id));
            return "empresa/hotelReserveView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/hotels/" + id;
        }
    }

    @GetMapping("/hotels/{idhotel}/reserves/{idres}")
    public String getReserveDetails(@PathVariable Long idhotel, @PathVariable Long idres, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Reserva reserva = reServ.getById(idres);
            reServ.verificarReserva(reserva);
            model.addAttribute("reserva", reserva);
            model.addAttribute("detalles", reserva.getHabitaciones());
            return "empresa/reserveDetailsView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/hotels/" + idhotel + "/reserves";
        }
    }

    // Cambiar estado de reservas
    @PostMapping("/hotels/{idhotel}/reserves/status")
    public String postEditReservStatus(@Valid @ModelAttribute("estadoReserva") EstadoReservaDTO dto,
            BindingResult bindingResult, @PathVariable Long idhotel, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
        } else {
            try {
                reServ.cambiarEstado(dto);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/enterprise/hotels/" + idhotel + "/reserves/" + dto.getId();
    }

    // nuevo hotel get
    @GetMapping("/hotels/new")
    public String getNewHotel(Model model, @ModelAttribute("formulario") HotelDTO dto) {
        if (dto == null || dto.getMunicipio() == null) {
            dto = new HotelDTO();
        }
        model.addAttribute("formulario", dto);
        return "empresa/hotelNewView";
    }

    // nuevo hotel post
    @PostMapping("/hotels/new/submit")
    public String postNewHotel(@Valid @ModelAttribute("formulario") HotelDTO dto, BindingResult bindingResult,
            Model model, @RequestParam MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("formulario", dto);
            return "redirect:/enterprise/hotels/new";
        } else {
            try {
                // revisar el redirect
                Hotel hotel = hoServ.agregar(dto, file);
                redirectAttributes.addFlashAttribute("message", "Hotel agregado con éxito.");
                return "redirect:/enterprise/hotels/" + hotel.getId();
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/enterprise/hotels/new";
            }
        }
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
            return "redirect:/enterprise/profile";
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
            return "redirect:/enterprise/hotels/edit/" + id;
        } else {
            try {
                hoServ.modificar(dto, id, file);
                redirectAttributes.addFlashAttribute("message", "Hotel modificado con éxito.");
                status.setComplete();
                return "redirect:/enterprise/hotels/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/enterprise/hotels/edit/" + id;
            }
        }
    }

    // editar estado habitacion post
    @PostMapping("/hotels/{id}/habitacion/status")
    public String postEditHabStatus(@Valid @ModelAttribute("estadoHabitacion") EstadoHabitacionDTO dto,
            BindingResult bindingResult, @PathVariable Long id,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
        } else {
            try {
                haServ.cambiarEstado(dto);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/enterprise/hotels/" + id;
    }

    // nueva habitacion get
    @GetMapping("/hotels/{id}/habitacion/new")
    public String getNewHab(Model model, @ModelAttribute("formulario") HabitacionDTO dto, @PathVariable Long id,
            SessionStatus status) {
        status.setComplete();
        model.addAttribute("hotelId", id);
        model.addAttribute("formulario", dto);
        return "empresa/habitacionNewView";
    }

    // nueva habitacion post
    @PostMapping("/hotels/{id}/habitacion/new/submit")
    public String postNewHabSubmit(@Valid @ModelAttribute("formulario") HabitacionDTO dto, BindingResult bindingResult,
            @PathVariable Long id, Model model, @RequestParam MultipartFile file, SessionStatus status,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("formulario", dto);
            return "redirect:/enterprise/hotels/" + id + "/habitacion/new";
        } else {
            try {
                hoServ.getById(id);
                haServ.agregar(dto, id, file);
                redirectAttributes.addFlashAttribute("message", "Habitación agregada con éxito.");
                status.setComplete();
                return "redirect:/enterprise/hotels/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/enterprise/hotels/" + id + "/habitacion/new";
            }
        }
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
            return "redirect:/enterprise/hotels/" + id;
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
            return "redirect:/enterprise/hotels/" + id + "/habitacion/edit/" + habId;
        } else {
            try {
                haServ.modificar(dto, habId, file);
                status.setComplete();
                redirectAttributes.addFlashAttribute("message", "Habitación modificada con éxito.");
                return "redirect:/enterprise/hotels/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/enterprise/hotels/" + id + "/habitacion/edit/" + habId;
            }
        }
    }
}
