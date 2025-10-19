package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelMiniDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.EmptyListException;
import com.hotelgalicia.proyectohotelgalicia.modelos.FiltroBusqueda;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HabitacionService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;
import com.hotelgalicia.proyectohotelgalicia.servicios.fileStorageService;

import jakarta.validation.Valid;

@Controller
public class MainController {
    @Autowired
    public fileStorageService fileserv;

    @Autowired
    private ClienteService cServ;

    @Autowired
    private EmpresaService eServ;

    @Autowired
    private HotelService hoServ;

    @Autowired
    private HabitacionService haServ;

    // Controlador principal, verifica listas vacías
    @GetMapping({ "/", "/home", "/index" })
    public String showHome(Model model, @ModelAttribute("searchform") HotelSearchDTO dto,
            @ModelAttribute List<HotelMiniDTO> listado) {
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
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(" | ")));
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
    public String getHotel(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("hotel", hoServ.getById(id));
            model.addAttribute("habitaciones", haServ.listHabitacionByHotelId(id));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/index";
        }
        return "hotelDetailView";
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
        return "/ChoiceView";
    }

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
        return "/registerView";
    }

    @PostMapping("/register/usuario/submit")
    public String postRegUser(@Valid ClienteDTO formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(" | ")));
            model.addAttribute("tipo", "usuario");
            model.addAttribute("fusuario", formulario);
            return "/registerView";
        }
        try {
            cServ.agregar(formulario);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fusuario", formulario);
            model.addAttribute("tipo", "usuario");
            return "public/registerView";
        }
        redirectAttributes.addFlashAttribute("message", "Usuario registrado con éxito.");
        return "redirect:/";
    }

    @PostMapping("/register/corp/submit")
    public String postAdd(@Valid EmpresaDTO formulario, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(" | ")));
            model.addAttribute("tipo", "empresa");
            model.addAttribute("fempresa", formulario);
            return "/registerView";
        }
        try {
            eServ.agregar(formulario);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fempresa", formulario);
            model.addAttribute("tipo", "empresa");
            return "public/registerView";
        }
        redirectAttributes.addFlashAttribute("message", "Usuario registrado con éxito.");
        return "redirect:/";
    }

    // Lista vacía por defecto
    private HotelSearchDTO defaultSearchDTO() {
        return new HotelSearchDTO(
                "", Municipios.TODOS, "", 1, 1,
                LocalDate.now(), LocalDate.now().plusDays(1),
                5, 10000, FiltroBusqueda.VALORACION_DESCENDENTE);
    }
}
