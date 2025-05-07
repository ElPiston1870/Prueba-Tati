package com.sistemaVeterinario.controller;

import com.sistemaVeterinario.dto.UsuarioDTO;
import com.sistemaVeterinario.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    public AuthController(AuthService authService, MessageSource messageSource) {
        this.authService = authService;
        this.messageSource = messageSource;
    }

    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "registrationForm";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "loginForm"; // Asegúrate de tener login.html en templates/
    }

    @PostMapping("/register")
    public String procesarRegistro(
            @Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
            BindingResult bindingResult,
            Model model
    ) {
        // Si hay errores de validación, volvemos al formulario
        if (bindingResult.hasErrors()) {
            return "registrationForm";
        }

        try {
            authService.registrarUsuario(usuarioDTO);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Manejo de errores específicos
            String mensaje = e.getMessage();

            if (mensaje.contains("email")) {
                model.addAttribute("emailError",
                        messageSource.getMessage("error.email.existing", null, LocaleContextHolder.getLocale()));
            } else if (mensaje.contains("teléfono")) {
                model.addAttribute("telefonoError",
                        messageSource.getMessage("error.phone.existing", null, LocaleContextHolder.getLocale()));
            }

            return "/registrationForm";
        }
    }
}