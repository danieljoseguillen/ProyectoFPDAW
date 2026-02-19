package com.hotelgalicia.proyectohotelgalicia.controladores;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.hotelgalicia.proyectohotelgalicia.servicios.FileStorageService;
import com.hotelgalicia.proyectohotelgalicia.servicios.HotelService;

import jakarta.validation.Valid;

@Controller
// @SessionAttributes({ "searchform" })
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
    private void validarfechasalert(HotelSearchDTO filtro, Model model) {
        if (filtro.getFechaFin().isBefore(filtro.getFechaInicio())
                || filtro.getFechaFin().isEqual(filtro.getFechaInicio())) {
            filtro.setFechaFin(filtro.getFechaInicio().plusDays(1));
            model.addAttribute("error", "La fecha de fin debe ser posterior a la fecha de inicio.");
        }

        if (filtro.getFechaInicio().isBefore(LocalDate.now())) {
            filtro.setFechaInicio(LocalDate.now());
            model.addAttribute("error", "La fecha de inicio no puede ser anterior a la fecha actual.");
        }

        if (filtro.getFechaFin().isBefore(LocalDate.now())) {
            filtro.setFechaFin(LocalDate.now().plusDays(1));
            model.addAttribute("error", "La fecha de fin no puede ser anterior a la fecha actual.");
        }
    }

    // Controlador principal, verifica listas vacías
    @GetMapping({ "/", "/home", "/index" })
    public String showHome(Model model,
            @ModelAttribute("searchform") HotelSearchDTO dto, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<HotelMiniDTO> hotelPage = Page.empty();
        try {
            validarfechasalert(dto, model);
            hotelPage = hoServ.listSortedHotel(dto, pageable);
        } catch (EmptyListException e) {
            hotelPage = new PageImpl<>(new ArrayList<>());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            HotelSearchDTO defecto = new HotelSearchDTO(
                    "", Municipios.TODOS, "", 1, 1,
                    LocalDate.now(), LocalDate.now().plusDays(1),
                    5, 10000, FiltroBusqueda.VALORACION_DESCENDENTE);
            hotelPage = hoServ.listSortedHotel(defecto, pageable);

        } finally {
            model.addAttribute("listado", hotelPage.getContent());
            model.addAttribute("currentPage", hotelPage.getNumber());
            model.addAttribute("totalPages", hotelPage.getTotalPages());
            model.addAttribute("totalItems", hotelPage.getTotalElements());
        }

        // model.addAttribute("listado", hoServ.listSortedHotel(dto));
        return "indexView";
    }

    // // Filtro de busqueda
    // @PostMapping("/search")
    // public String postSearchResults(@Valid @ModelAttribute("searchform")
    // HotelSearchDTO dto,
    // BindingResult bindingResult, Model model,
    // RedirectAttributes redirectAttributes) {
    // HotelSearchDTO defecto = new HotelSearchDTO(
    // "", Municipios.TODOS, "", 1, 1,
    // LocalDate.now(), LocalDate.now().plusDays(1),
    // 5, 10000, FiltroBusqueda.VALORACION_DESCENDENTE);
    // if (bindingResult.hasErrors()) {
    // model.addAttribute("searchform", dto);
    // model.addAttribute("listado", hoServ.listSortedHotel(defecto));
    // return "indexView";
    // }
    // try {
    // validarfechasalert(dto, model);
    // model.addAttribute("listado", hoServ.listSortedHotel(dto));
    // } catch (EmptyListException e) {
    // model.addAttribute("warning", e.getMessage());
    // model.addAttribute("listado", hoServ.listSortedHotel(defecto));
    // } catch (Exception e) {
    // model.addAttribute("error", e.getMessage());
    // model.addAttribute("listado", hoServ.listSortedHotel(defecto));
    // }
    // return "indexView";

    // }

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

    @GetMapping("/signin")
    public String showLogin() {
        return "signInView";
    }
}
