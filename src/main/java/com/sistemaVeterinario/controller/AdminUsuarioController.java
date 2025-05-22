package com.sistemaVeterinario.controller;

import com.sistemaVeterinario.dto.UsuarioDTO;
import com.sistemaVeterinario.models.Role;
import com.sistemaVeterinario.models.Usuario;
import com.sistemaVeterinario.service.AdminUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private AdminUsuarioService adminUsuarioService;

    /**
     * Muestra el listado de usuarios con opción de búsqueda
     */
    @GetMapping
    public String listarUsuarios(@RequestParam(required = false) String search, Model model) {
        List<Usuario> usuarios;
        if (search != null && !search.isEmpty()) {
            usuarios = adminUsuarioService.searchUsuarios(search);
            model.addAttribute("search", search);
        } else {
            usuarios = adminUsuarioService.getAllUsuarios();
        }
        model.addAttribute("usuarios", usuarios);
        return "admin/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("roles", adminUsuarioService.getAllRoles());
        model.addAttribute("esNuevo", true);
        return "admin/formulario";
    }

    /**
     * Procesa la creación de un nuevo usuario
     */
    @PostMapping("/nuevo")
    public String crearUsuario(@Valid @ModelAttribute("usuario") UsuarioDTO usuario,
                               BindingResult result,
                               @RequestParam(required = false) Set<Integer> rolesIds,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("esNuevo", true);
            return "admin/formulario";
        }

        if (rolesIds == null || rolesIds.isEmpty()) {
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("esNuevo", true);
            model.addAttribute("errorRoles", "Debe seleccionar al menos un rol");
            return "admin/formulario";
        }

        try {
            adminUsuarioService.createUsuario(usuario, rolesIds);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            return "redirect:/admin/usuarios";
        } catch (DataIntegrityViolationException e) {
            // Manejo específico para violación de datos (ej: duplicados)
            String errorMessage = "Error de datos: ";
            if (e.getMessage().contains(usuario.getTelefono())) {
                errorMessage += "El teléfono ya está registrado";
            } else if (e.getMessage().contains(usuario.getEmail())) {
                errorMessage += "El email ya está registrado";
            } else {
                errorMessage += "Datos inválidos o duplicados";
            }
            model.addAttribute("error", errorMessage);
        } catch (IllegalArgumentException e) {
            // Manejo para parámetros inválidos
            model.addAttribute("error", "Datos inválidos: " + e.getMessage());
        } catch (Exception e) {
            // Manejo genérico para otros errores

            model.addAttribute("error", "Error inesperado al crear el usuario");
        } finally {
            // Prepara el modelo para volver a mostrar el formulario
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("esNuevo", true);
            model.addAttribute("usuario", usuario); // Mantener los datos ingresados
        }

        return "admin/formulario";
    }

    /**
     * Muestra el formulario para editar un usuario existente
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Optional<Usuario> optionalUsuario = adminUsuarioService.getUsuarioById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            UsuarioDTO usuarioDTO = adminUsuarioService.convertToDto(usuario);

            model.addAttribute("usuario", usuarioDTO);
            model.addAttribute("usuarioId", id);
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("usuarioRoles", usuario.getRoles());
            model.addAttribute("esNuevo", false);

            return "admin/formulario";
        } else {
            return "redirect:/admin/usuarios";
        }
    }

    /**
     * Procesa la actualización de un usuario existente
     */
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Integer id,
                                    @Valid @ModelAttribute("usuario") Usuario usuario,
                                    BindingResult result,
                                    @RequestParam(required = false) Set<Integer> rolesIds,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("usuarioId", id);
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("esNuevo", false);
            return "admin/formulario";
        }

        if (rolesIds == null || rolesIds.isEmpty()) {
            model.addAttribute("usuarioId", id);
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("esNuevo", false);
            model.addAttribute("errorRoles", "Debe seleccionar al menos un rol");
            return "admin/formulario";
        }

        try {
            adminUsuarioService.updateUsuario(id, usuario, rolesIds);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            model.addAttribute("usuarioId", id);
            model.addAttribute("roles", adminUsuarioService.getAllRoles());
            model.addAttribute("esNuevo", false);
            return "admin/formulario";
        }
    }
}