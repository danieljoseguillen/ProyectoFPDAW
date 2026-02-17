package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.DetalleReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HabitacionListDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDateDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ValoracionDTO;
import com.hotelgalicia.proyectohotelgalicia.servicios.FileStorageService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ReservaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.ValoracionService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "reserva", "filtro" })
public class hotelController {
    @Autowired
    public FileStorageService fileserv;

    @Autowired
    private HotelService hoServ;

    @Autowired
    private HabitacionService haServ;

    @Autowired
    private ValoracionService vaServ;

    @Autowired
    private ReservaService reServ;

    private String formatBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));
    }

    // Lista vacía por defecto
    // private HotelDateDTO defaultDateDTO() {
    // return new HotelDateDTO(
    // LocalDate.now(), LocalDate.now().plusDays(1), 1);
    // }

    // @ModelAttribute("filtro")
    // public HotelDateDTO filtro() {
    // return defaultDateDTO();
    // }

    // @ModelAttribute("reserva")
    // public ReservaDTO reserva() {
    // return new ReservaDTO();
    // }

    // Valida fechas y da mensajes de error
    private void validarfechas(HotelDateDTO filtro) {
        if (filtro.getFechaFin().isBefore(filtro.getFechaInicio())
                || filtro.getFechaFin().isEqual(filtro.getFechaInicio())) {
            filtro.setFechaFin(filtro.getFechaInicio().plusDays(1));
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }

        if (filtro.getFechaInicio().isBefore(LocalDate.now())) {
            filtro.setFechaInicio(LocalDate.now());
            throw new RuntimeException("La fecha de inicio no puede ser anterior a la fecha actual.");
        }

        if (filtro.getFechaFin().isBefore(LocalDate.now())) {
            filtro.setFechaFin(LocalDate.now().plusDays(1));
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha actual.");
        }
    }

    // Muestra hoteles
    @GetMapping("/hotel/{id}")
    public String getHotel(@PathVariable Long id,
            @ModelAttribute("filtro") HotelDateDTO filtro, Model model,
            RedirectAttributes redirectAttributes,
            Principal principal, SessionStatus status) {
        try {
            try {
                validarfechas(filtro);
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }

            // Hotel y habitaciones
            Hotel hotel = hoServ.getById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                model.addAttribute("habitaciones", hotel.getHabitaciones());
            } else {
                List<HabitacionListDTO> habitaciones = haServ.listHabitacionByHotelIdDisponibles(id,
                        filtro.getFechaInicio(),
                        filtro.getFechaFin());
                if (filtro.getFechaInicio() == null) {
                    filtro.setFechaInicio(LocalDate.now());
                    filtro.setFechaFin(LocalDate.now().plusDays(1));
                    filtro.setPersonas(1);
                }
                model.addAttribute("habitaciones", habitaciones);
                model.addAttribute("filtro", filtro);
                // Completa el formulario de estár vacío.
                if (((ReservaDTO) model.getAttribute("reserva")).getId() == null
                        || !((ReservaDTO) model.getAttribute("reserva")).getId().equals(hotel.getId())) {
                    status.setComplete();
                    ReservaDTO reserva = new ReservaDTO();
                    reserva.setId(hotel.getId());
                    reserva.setFechaInicio(filtro.getFechaInicio());
                    reserva.setFechaFin(filtro.getFechaFin());
                    reserva.setPersonas(filtro.getPersonas());
                    List<DetalleReservaDTO> detalles = habitaciones.stream()
                            .map(h -> new DetalleReservaDTO(h.getId(), 0))
                            .toList();
                    reserva.setHabitaciones(detalles);
                    model.addAttribute("reserva", reserva);
                }
            }
            model.addAttribute("hotel", hotel);
            model.addAttribute("valoracionestotales", hotel.getValoracion().size());
            model.addAttribute("valoraciones", hotel.getValoracion());
            model.addAttribute("valoracion", new ValoracionDTO());
            // if (vaServ.getByIds(retornarId(), id) != null) {
            // model.addAttribute("myval", vaServ.getByIds(retornarId(), id));
            // }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
        return "hotelDetailView";
    }

    @PostMapping("/hotel/{id}/reserve")
    public String postHotelReserve(@Valid @ModelAttribute("reserva") ReservaDTO reserva, BindingResult bindingResult,
            @PathVariable Long id,
            @ModelAttribute("filtro") HotelDateDTO filtro,
            Model model, RedirectAttributes redirectAttributes, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Hay errores en el formulario de reserva.");
        } else if (!reServ.verificarCantidad(reserva)) {
            redirectAttributes.addFlashAttribute("error", "Debe reservar al menos una habitación.");
        } else if (((ReservaDTO) model.getAttribute("reserva")).getId() == null
                || !((ReservaDTO) model.getAttribute("reserva")).getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "Error en la reserva, por favor intente de nuevo.");
        } else {
            try {
                try {
                    validarfechas(filtro);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("error", e.getMessage());
                    return "redirect:/hotel/" + id;
                }
                // Actualizar personas con el valor del filtro
                reserva.setPersonas(filtro.getPersonas());
                reserva.setFechaInicio(filtro.getFechaInicio());
                reserva.setFechaFin(filtro.getFechaFin());
                List<HabitacionListDTO> habitaciones = haServ.listHabitacionByHotelIdDisponibles(id,
                        reserva.getFechaInicio(),
                        reserva.getFechaFin());
                model.addAttribute("habitaciones", habitaciones);
                int totalHabis = reserva.getHabitaciones().stream()
                        .mapToInt(DetalleReservaDTO::getCantidad)
                        .sum();
                model.addAttribute("totalHabis", totalHabis);
                int totalprice = reServ.calcularPrecioTotal(reserva).intValue();
                model.addAttribute("totalprice", totalprice);
                model.addAttribute("dias", ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin()));
                model.addAttribute("hotel", hoServ.ConvertHotelToDTO(hoServ.getById(id)));
                return "reserve/reservaConfirmView";
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
}
