package com.sistemaVeterinario.controller;

import com.sistemaVeterinario.dto.UsuarioDTO;
import com.sistemaVeterinario.models.Servicio;
import com.sistemaVeterinario.service.AuthService;
import com.sistemaVeterinario.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PublicController {

    @Autowired
    private ServicioService servicioService;

    private final AuthService authService;
    private final MessageSource messageSource;

    public PublicController(AuthService authService, MessageSource messageSource) {
        this.authService = authService;
        this.messageSource = messageSource;
    }



    @GetMapping("/")
    public String home(Model model) {
        List<Servicio> servicios = servicioService.findAllActive();
        model.addAttribute("servicios", servicios);
        return "public/home";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "public/aboutUs";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "public/loginForm";
    }

    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "/public/registrationForm";
    }

    @PostMapping("/register")
    public String procesarRegistro(
            @Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
            BindingResult bindingResult,
            Model model
    ) {
        // Si hay errores de validación, volvemos al formulario
        if (bindingResult.hasErrors()) {
            return "public/registrationForm";
        }

        try {
            authService.registrarUsuario(usuarioDTO);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Manejo de errores específicos
            String mensaje = e.getMessage();

            if (mensaje.contains("correo")) {
                model.addAttribute("emailError",
                        messageSource.getMessage("error.email.existing", null, LocaleContextHolder.getLocale()));
            } else if (mensaje.contains("teléfono")) {
                model.addAttribute("telefonoError",
                        messageSource.getMessage("error.phone.existing", null, LocaleContextHolder.getLocale()));
            }
            return "public/registrationForm";
        }
    }
}