package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.time.LocalDate;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotelgalicia.proyectohotelgalicia.dto.ClienteDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.EmpresaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.excepciones.EmptyListException;
import com.hotelgalicia.proyectohotelgalicia.servicios.ClienteService;
import com.hotelgalicia.proyectohotelgalicia.servicios.EmpresaService;
import com.hotelgalicia.proyectohotelgalicia.servicios.FileStorageService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "searchform" })
public class MainController {
    @Autowired
    public FileStorageService fileserv;

    @Autowired
    private ClienteService cServ;

    @Autowired
    private EmpresaService eServ;

    @Autowired
    private HotelService hoServ;

    // Valida fechas y da mensajes de error
    private void validarfechasalert(HotelSearchDTO filtro) {
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
        private void validarfechas(HotelSearchDTO filtro) {
        if (filtro.getFechaFin().isBefore(filtro.getFechaInicio())
                || filtro.getFechaFin().isEqual(filtro.getFechaInicio())) {
            filtro.setFechaFin(filtro.getFechaInicio().plusDays(1));
        }

        if (filtro.getFechaInicio().isBefore(LocalDate.now())) {
            filtro.setFechaInicio(LocalDate.now());
        }

        if (filtro.getFechaFin().isBefore(LocalDate.now())) {
            filtro.setFechaFin(LocalDate.now().plusDays(1));
        }
    }

    // Lista vacía por defecto
    // COMENTADO: SessionAttributeAdvice proporciona searchform automáticamente
    // private HotelSearchDTO defaultSearchDTO() {
    //     return new HotelSearchDTO(
    //             "", Municipios.TODOS, "", 1, 1,
    //             LocalDate.now(), LocalDate.now().plusDays(1),
    //             5, 10000, FiltroBusqueda.VALORACION_DESCENDENTE);
    // }

    // Controlador principal, verifica listas vacías
    @GetMapping({ "/", "/home", "/index" })
    public String showHome(Model model,
            @ModelAttribute("searchform") HotelSearchDTO dto) {
        try {
            validarfechasalert(dto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        // COMENTADO: defaultSearchDTO() movido a SessionAttributeAdvice
        // model.addAttribute("listado", hoServ.listSortedHotel(defaultSearchDTO()));
        model.addAttribute("listado", hoServ.listSortedHotel(dto));
        return "indexView";
    }

    // Filtro de busqueda
    @PostMapping("/search")
    public String postSearchResults(@Valid @ModelAttribute("searchform") HotelSearchDTO dto,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // redirectAttributes.addFlashAttribute("error",
            // formatBindingErrors(bindingResult));
            validarfechas(dto);
            model.addAttribute("searchform", dto);
            // COMENTADO: defaultSearchDTO() movido a SessionAttributeAdvice
            // model.addAttribute("listado", hoServ.listSortedHotel(defaultSearchDTO()));
            model.addAttribute("listado", hoServ.listSortedHotel(dto));
            return "indexView";
        } else {
            try {
                validarfechasalert(dto);
                model.addAttribute("listado", hoServ.listSortedHotel(dto));
            } catch (EmptyListException e) {
                model.addAttribute("warning", e.getMessage());
                // COMENTADO: defaultSearchDTO() movido a SessionAttributeAdvice
                // model.addAttribute("listado", hoServ.listSortedHotel(defaultSearchDTO()));
                model.addAttribute("listado", hoServ.listSortedHotel(dto));
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
            model.addAttribute("searchform", dto);
            return "indexView";
        }
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
    public String postRegUser(@Valid @ModelAttribute("fusuario") ClienteDTO formulario, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // model.addAttribute("error", formatBindingErrors(bindingResult));
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
    @PostMapping("/register/empresa/submit")
    public String postAdd(@Valid @ModelAttribute("fempresa") EmpresaDTO formulario, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // model.addAttribute("error", formatBindingErrors(bindingResult));
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
