package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.DetalleReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionListDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ValoracionDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.EmptyListException;
import com.hotelgalicia.proyectohotelgalicia.modelos.FiltroBusqueda;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.FileStorageService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ValoracionService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "reserva", "hotelId" })
public class MainController {
    @Autowired
    public FileStorageService fileserv;

    @Autowired
    private ClienteService cServ;

    @Autowired
    private EmpresaService eServ;

    @Autowired
    private HotelService hoServ;

    @Autowired
    private HabitacionService haServ;

    @Autowired
    private ValoracionService vaServ;

    @Autowired
    private ReservaService reServ;

    // Retorna la id del usuario.
    private Long retornarId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return null;
        }
        return cServ.getByCorreo(authentication.getName()).getId();
    }

    private String formatBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));
    }

    // Lista vacía por defecto
    private HotelSearchDTO defaultSearchDTO() {
        return new HotelSearchDTO(
                "", Municipios.TODOS, "", 1, 1,
                LocalDate.now(), LocalDate.now().plusDays(1),
                5, 10000, FiltroBusqueda.VALORACION_DESCENDENTE);
    }

    // Controlador principal, verifica listas vacías
    @GetMapping({ "/", "/home", "/index" })
    public String showHome(Model model, @ModelAttribute("searchform") HotelSearchDTO dto,
            @ModelAttribute("listado") List<HotelMiniDTO> listado) {
        if (dto == null || dto.getMunicipio() == null) {
            dto = defaultSearchDTO();
        }
        model.addAttribute("searchform", dto);
        if (listado == null || listado.isEmpty()) {
            model.addAttribute("listado", hoServ.listSortedHotel(defaultSearchDTO()));
        }
        return "indexView";
    }

    // Filtro de busqueda
    @PostMapping("/search")
    public String postSearchResults(@Valid @ModelAttribute("searchform") HotelSearchDTO dto,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            redirectAttributes.addFlashAttribute("searchform", dto);
            return "redirect:/index";
        } else {
            try {
                redirectAttributes.addFlashAttribute("listado", hoServ.listSortedHotel(dto));
            } catch (EmptyListException e) {
                redirectAttributes.addFlashAttribute("warning", e.getMessage());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            redirectAttributes.addFlashAttribute("searchform", dto);
            return "redirect:/index";
        }
    }

    // Muestra hoteles
    @GetMapping("/hotel/{id}")
    public String getHotel(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            Principal principal, SessionStatus status) {
        try {
            // Hotel y habitaciones
            Hotel hotel = hoServ.getById(id);
            List<HabitacionListDTO> habitaciones = haServ.listHabitacionByHotelId(id);

            // Completa el formulario de estár vacío.
            if (model.getAttribute("hotelId") == null || !model.getAttribute("hotelId").equals(id)) {
                status.setComplete();
                ReservaDTO reserva = new ReservaDTO();
                List<DetalleReservaDTO> detalles = habitaciones.stream()
                        .map(h -> new DetalleReservaDTO(h.getId(), 0))
                        .toList();
                reserva.setHabitaciones(detalles);
                model.addAttribute("hotelId", hotel.getId());
                model.addAttribute("reserva", reserva);
            }
            model.addAttribute("hotel", hotel);
            model.addAttribute("habitaciones", habitaciones);
            model.addAttribute("valoraciones", vaServ.listByHotelId(id));
            model.addAttribute("valoracion", new ValoracionDTO());
            model.addAttribute("myval", vaServ.getByIds(retornarId(), id));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
        return "hotelDetailView";
    }

    @PostMapping("/hotel/{id}/reserve")
    public String postHotelReserve(@Valid @ModelAttribute("reserva") ReservaDTO reserva, BindingResult bindingResult,
            @PathVariable Long id,
            Model model, RedirectAttributes redirectAttributes, SessionStatus status) {
        model.addAttribute("reserva", reserva);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
        } else if (!reServ.verificarCantidad(reserva)) {
            redirectAttributes.addFlashAttribute("error", "Debe reservar al menos una habitación.");
        } else {
            try {
                model.addAttribute("hotel", hoServ.ConvertHotelToDTO(hoServ.getById(id)));
                return "cliente/ReservaConfirmView";
            } catch (Exception e) {
                status.setComplete();
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/hotel/" + id;
    }

    @PostMapping("/hotel/{id}/reserve/submit")
    public String postHotelReserveSubmit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @ModelAttribute("reserva") ReservaDTO reserva, SessionStatus status) {
        try {
            reServ.agregar(reserva, id);
            redirectAttributes.addFlashAttribute("message", "Reserva realizada con exito.");
            status.setComplete();
            return "redirect:/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/hotel/" + id;
        }
    }

    @PostMapping("/hotel/{id}/valoration")
    public String postNewValoration(@Valid ValoracionDTO valoracion, BindingResult bindingResult, @PathVariable Long id,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
        } else {
            try {
                vaServ.agregar(valoracion);
                redirectAttributes.addFlashAttribute("message", "Reseña agregada con exito.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        return "redirect:/hotel/" + id;
    }

    // Envía a la pagina de error
    @GetMapping("/accessError")
    public String getErrorView() {
        return "error/accessErrorView";
    }

    // Carga de imagenes en lista
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileserv.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }

    // Zona Registro
    @GetMapping("/register")
    public String getReg(Model model) {
        return "ChoiceView";
    }

    // Registro por tipo
    @GetMapping("/register/{type}")
    public String getRegType(@PathVariable String type, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("tipo", type);
        switch (type) {
            case "usuario" -> {
                model.addAttribute("fusuario", new ClienteDTO());
            }
            case "empresa" -> {
                model.addAttribute("fempresa", new EmpresaDTO());
            }
            default -> {
                redirectAttributes.addFlashAttribute("error", "Tipo de registro no válido.");
                return "redirect:/register";
            }
        }
        return "registerView";
    }

    // Registro Usuario
    @PostMapping("/register/usuario/submit")
    public String postRegUser(@Valid ClienteDTO formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            model.addAttribute("tipo", "usuario");
            model.addAttribute("fusuario", formulario);
            return "registerView";
        }
        try {
            cServ.agregar(formulario);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fusuario", formulario);
            model.addAttribute("tipo", "usuario");
            return "registerView";
        }
        redirectAttributes.addFlashAttribute("message", "Usuario registrado con éxito.");
        return "redirect:/";
    }

    // Registro Empresa
    @PostMapping("/register/corp/submit")
    public String postAdd(@Valid EmpresaDTO formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", formatBindingErrors(bindingResult));
            model.addAttribute("tipo", "empresa");
            model.addAttribute("fempresa", formulario);
            return "registerView";
        }
        try {
            eServ.agregar(formulario);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fempresa", formulario);
            model.addAttribute("tipo", "empresa");
            return "registerView";
        }
        redirectAttributes.addFlashAttribute("message", "Usuario registrado con éxito.");
        return "redirect:/";
    }
}
