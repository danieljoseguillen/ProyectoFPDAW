package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.DetalleReserva;
import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Habitacion;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.domain.Reserva;
import com.hotelgalicia.proyectohotelgalicia.dto.ClaveDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.CorreoDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EstadoReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaListDTO;
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

    private String formatBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));
    }

    // profile get
    @GetMapping("/profile")
    public String getProfile(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Empresa emp = eServ.getByCorreo(authentication.getName());
            model.addAttribute("empresa", emp);
            List<Hotel> hoteles = hoServ.listHotelByCorpo(emp.getId());
            model.addAttribute("hoteles", hoteles);
            return "empresa/empresaProfileView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
    }

    // edit profile get
    @GetMapping("/editmail")
    public String getedit(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("correo",
                    modelMapper.map(eServ.getByCorreo(authentication.getName()), EmpresaDTO.class));
            return "empresa/changeMailView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
    }

    // edit profile post
    @PostMapping("/editmail/submit")
    public String postedit(@Valid @ModelAttribute("correo") CorreoDTO correo, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "empresa/changeMailView";
        }
        try {
            eServ.modificarCorreo(correo);
            redirectAttributes.addFlashAttribute("message", "Correo actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enterprise/profile";
    }

    @GetMapping("/password")
    public String getPasswordChange(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("formulario", new ClaveDTO(null, null, null));
            return "empresa/changePasswordView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
    }

    @PostMapping("/password/submit")
    public String postPasswordChange(@Valid @ModelAttribute("formulario") ClaveDTO formulario,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "empresa/changePasswordView";
        }
        try {
            eServ.cambiarContraseñaPorId(formulario);
            redirectAttributes.addFlashAttribute("message", "Contraseña actualizada con exito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enterprise/profile";
    }

    @GetMapping("/hotels")
    public String getHotels(Model model, RedirectAttributes redirectAttributes,
            SessionStatus status) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Empresa emp = eServ.getByCorreo(authentication.getName());
            List<Hotel> hoteles = hoServ.listHotelByCorpo(emp.getId());
            model.addAttribute("hoteles", hoteles);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
        return "empresa/hotelEnterpriseListView";
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
            model.addAttribute("habitaciones", hotel.getHabitaciones().stream()
                    .filter(hab -> hab.getEstado().name() != "ELIMINADA")
                    .toList());
            model.addAttribute("valoracionestotales", hotel.getValoracion().size());
            model.addAttribute("valoraciones", hotel.getValoracion());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
        return "empresa/hotelEnterpriseView";
    }

    // Ver reservas de hotel
    @GetMapping("/hotels/{id}/reserves")
    public String getHotelReserves(@PathVariable Long id, Model model,@RequestParam(defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
        Pageable pageable = PageRequest.of(page, 6);
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            // Crea las reservas como reservalistdto
            Page<ReservaListDTO> reservas = reServ.listarReservasHotel(id, pageable);
            model.addAttribute("hotel", hotel);
            model.addAttribute("reservas", reservas);
            model.addAttribute("currentPage", reservas.getNumber());
            model.addAttribute("totalPages", reservas.getTotalPages());
            model.addAttribute("totalItems", reservas.getTotalElements());
            return "reserve/reserveListView";
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
            int totalHabis = reserva.getHabitaciones().stream()
                    .mapToInt(DetalleReserva::getCantidad)
                    .sum();
            model.addAttribute("totalHabis", totalHabis);
            int totalprice = reServ.calcularPrecioTotal(reserva).intValue();
            model.addAttribute("totalprice", totalprice);
            model.addAttribute("dias", ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin()));
            model.addAttribute("estadoReserva", new EstadoReservaDTO());
            return "reserve/reserveDetailsView";
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
        // bindingResult.rejectValue("municipio", "municipio.error", "Debe Seleccionar
        // un municipio.");
        if (bindingResult.hasErrors()) {
            model.addAttribute("formulario", dto);
            return "empresa/hotelNewView";
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
    @GetMapping("/hotels/{id}/edit")
    public String getEditHotel(Model model,
            RedirectAttributes redirectAttributes, @PathVariable Long id, SessionStatus status) {
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

            model.addAttribute("hotelId", id);
            model.addAttribute("formulario", dto);
            return "empresa/hotelEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/hotels/" + id;
        }
    }

    // editar hotel post
    @PostMapping("/hotels/edit/submit")
    public String postEditHotel(@Valid @ModelAttribute("formulario") HotelDTO dto, BindingResult bindingResult,
            Model model, @RequestParam MultipartFile file, @ModelAttribute("hotelId") Long id,
            RedirectAttributes redirectAttributes, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formulario", dto);
            return "empresa/hotelEditView";
        } else {
            try {
                hoServ.modificar(dto, id, file);
                redirectAttributes.addFlashAttribute("message", "Hotel modificado con éxito.");
                status.setComplete();
                return "redirect:/enterprise/hotels/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                redirectAttributes.addFlashAttribute("formulario", dto);
                return "redirect:/enterprise/hotels/" + id + "/edit";
            }
        }
    }

    // editar estado habitacion post
    @PostMapping("/hotels/{id}/habitacion/status/{habId}")
    public String postEditHabStatus(@PathVariable Long habId,
            @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            haServ.cambiarEstado(habId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/enterprise/hotels/" + id;
    }

    @PostMapping("/hotels/{id}/habitacion/delete/{habId}")
    public String postElimHabStatus(@PathVariable Long habId,
            @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            haServ.DesabilitarPorId(habId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enterprise/hotels/" + id;
    }

    // nueva habitacion get
    @GetMapping("/hotels/{id}/habitacion/new")
    public String getNewHab(Model model, @ModelAttribute("formulario") HabitacionDTO dto, @PathVariable Long id,
            SessionStatus status) {
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
            model.addAttribute("hotelId", id);
            model.addAttribute("formulario", dto);
            return "empresa/habitacionNewView";
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
            model.addAttribute("formulario", dto);
            model.addAttribute("habitacionId", habId);
            return "empresa/habitacionEditView";
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
