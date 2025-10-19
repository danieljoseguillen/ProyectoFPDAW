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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.domain.Empresa;
import com.hotelgalicia.proyectohotelgalicia.domain.Hotel;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelDTO;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/enterprise")
@SessionAttributes("hotelId")
public class CorpoController {

    @Autowired
    private EmpresaService eServ;

    @Autowired
    private HotelService hoServ;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Empresa emp = eServ.getByCorreo(authentication.getName());
        model.addAttribute("empresa", emp);
        List<Hotel> hoteles = hoServ.listHotelByCorpo(emp.getId());
        model.addAttribute("hoteles", hoteles);
        return "enterprise/empresaProfileView";
    }

    @GetMapping("/editprofile")
    public String getedit(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("empresa",
                    modelMapper.map(eServ.getByCorreo(authentication.getName()), EmpresaDTO.class));
            return "enterprise/empresaEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }

    }

    @PostMapping("/editprofile/submit")
    public String postedit(@Valid EmpresaDTO empresa, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(" | ")));
            redirectAttributes.addFlashAttribute("empresa", empresa);
            return "redirect:/enterprise/editprofile";
        }
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            eServ.modificar(empresa, eServ.getByCorreo(authentication.getName()).getId());
            redirectAttributes.addFlashAttribute("message", "Datos actualizados con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enterprise/profile";

    }

    @GetMapping("/hotels/new")
    public String getNewHotel(Model model, @ModelAttribute("formulario") HotelDTO dto,
            RedirectAttributes redirectAttributes) {
        if (dto == null || dto.getMunicipio() == null) {
            dto = new HotelDTO();
        }
        model.addAttribute("formulario", dto);
        return "enterprise/hotelNewView";
    }

    @PostMapping("/hotels/new/submit")
    public String postNewHotel(@Valid @ModelAttribute("formulario") HotelDTO dto, BindingResult bindingResult,
            Model model, @RequestParam MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(" | ")));
            redirectAttributes.addFlashAttribute("formulario", dto);
            return "redirect:/enterprise/hotels/new";
        } else {
            try {
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

    @GetMapping("/hotels/edit/{id}")
    public String getEditHotel(Model model,
            RedirectAttributes redirectAttributes, @PathVariable Long id) {
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);
            model.addAttribute("formulario", dto);
            return "enterprise/hotelNewView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
    }

    @PostMapping("/hotels/edit/submit")
    public String postEditHotel(@Valid @ModelAttribute("formulario") HotelDTO dto, BindingResult bindingResult,
            Model model, @RequestParam MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(" | ")));
            redirectAttributes.addFlashAttribute("formulario", dto);
            return "redirect:/enterprise/hotels/new";
        } else {
            try {
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

    @GetMapping("/hotels/{id}")
    public String getHotel(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Hotel hotel = hoServ.getById(id);
            hoServ.verificarHotel(hotel);
            model.addAttribute("hotel", hotel);
            model.addAttribute("habitaciones", hotel.getHabitaciones());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enterprise/profile";
        }
        return "enterprise/hotelEnterpriseView";
    }

}
