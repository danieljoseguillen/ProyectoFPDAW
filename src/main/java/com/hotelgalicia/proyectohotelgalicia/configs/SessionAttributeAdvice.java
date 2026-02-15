package com.hotelgalicia.proyectohotelgalicia.configs;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hotelgalicia.proyectohotelgalicia.dto.HotelDateDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.HotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.ReservaDTO;
import com.hotelgalicia.proyectohotelgalicia.dto.SimpleHotelSearchDTO;
import com.hotelgalicia.proyectohotelgalicia.modelos.FiltroBusqueda;
import com.hotelgalicia.proyectohotelgalicia.modelos.FiltroBusquedaAdmin;
import com.hotelgalicia.proyectohotelgalicia.modelos.Municipios;

@ControllerAdvice
public class SessionAttributeAdvice {

    /**
     * Formulario de búsqueda por defecto.
     * Se usa en MainController, se espera que esté disponible
     * siempre que se renderice indexView.html o se redirija /.
     */

    @ModelAttribute("searchform")
    public HotelSearchDTO searchForm() {
        return new HotelSearchDTO(
                "", Municipios.TODOS, "", 1, 1,
                LocalDate.now(), LocalDate.now().plusDays(1),
                5, 10000, FiltroBusqueda.VALORACION_DESCENDENTE);
    }

    @ModelAttribute("searchformadm")
    public SimpleHotelSearchDTO searchFormAdmin() {
        return new SimpleHotelSearchDTO(
                "", Municipios.TODOS, "", FiltroBusquedaAdmin.NOMBRE_ASCENDENTE);
    }
    /**
     * Valor por defecto para reserva.
     * Se usa en hotelController.
     */
    @ModelAttribute("reserva")
    public ReservaDTO reserva() {
        return new ReservaDTO();
    }


    /**
     * Valor por defecto para filtro en hotelController.
     */
    private HotelDateDTO defaultDateDTO() {
        return new HotelDateDTO(
                LocalDate.now(), LocalDate.now().plusDays(1), 1);
    }

    @ModelAttribute("filtro")
    public HotelDateDTO filtro() {
        return defaultDateDTO();
    }

    /**
     * Valor por defecto para reservaId en ClienteController.
     */
    @ModelAttribute("reservaId")
    public Long defaultReservaId() {
        return null;
    }

    /**
     * Valor por defecto para habitacionId en CorpoController.
     */
    @ModelAttribute("habitacionId")
    public Long defaultHabitacionId() {
        return null;
    }

    /**
     * Valor por defecto para userid en AdminController.
     */
    @ModelAttribute("userid")
    public Long defaultUserid() {
        return null;
    }

    /**
     * Valor por defecto para hotelid en AdminController.
     */
    @ModelAttribute("hotelid")
    public Long defaultHotelid() {
        return null;
    }

}
