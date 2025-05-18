package com.sistemaVeterinario.controller;

import com.sistemaVeterinario.models.Mascota;
import com.sistemaVeterinario.models.Usuario;
import com.sistemaVeterinario.service.MascotaService;
import com.sistemaVeterinario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarMascotas(Model model) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        List<Mascota> mascotas = mascotaService.findByPropietario(usuarioActual);
        model.addAttribute("mascotas", mascotas);
        return "mascotas/listaMascota";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaMascota(Model model) {
        model.addAttribute("mascota", new Mascota());
        model.addAttribute("titulo", "Agregar Nueva Mascota");
        return "mascotas/formMascota";
    }

    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute Mascota mascota,
                                 @RequestParam("fechaNacimientoStr") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "mascotas/formMascota";
        }

        // Obtener el usuario autenticado y asignarlo como propietario
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        if (!usuarioActual.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la mascota: usuario no autenticado");
            return "redirect:/mascotas";
        }

        mascota.setPropietario(usuarioActual.get());
        mascota.setFechaNacimiento(fechaNacimiento);

        mascotaService.save(mascota);
        redirectAttributes.addFlashAttribute("success", "Mascota guardada con éxito");
        return "redirect:/mascotas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMascota(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        Mascota mascota = mascotaService.findById(id);

        // Verificar que la mascota exista y pertenezca al usuario
        if (mascota == null) {
            redirectAttributes.addFlashAttribute("error", "La mascota no existe");
            return "redirect:/mascotas";
        }

        if (!usuarioActual.isPresent() || !mascota.getPropietario().getIdUsuario().equals(usuarioActual.get().getIdUsuario())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta mascota");
            return "redirect:/mascotas";
        }

        model.addAttribute("mascota", mascota);
        model.addAttribute("titulo", "Editar Mascota");
        model.addAttribute("fechaNacimientoStr", mascota.getFechaNacimiento());
        return "mascotas/formMascota";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        Mascota mascota = mascotaService.findById(id);

        // Verificar que la mascota exista y pertenezca al usuario
        if (mascota == null) {
            redirectAttributes.addFlashAttribute("error", "La mascota no existe");
            return "redirect:/mascotas";
        }

        if (!usuarioActual.isPresent() || !mascota.getPropietario().getIdUsuario().equals(usuarioActual.get().getIdUsuario())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta mascota");
            return "redirect:/mascotas";
        }

        mascotaService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Mascota eliminada con éxito");
        return "redirect:/mascotas";
    }

    @GetMapping("/detalles/{id}")
    public String verDetallesMascota(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        Mascota mascota = mascotaService.findById(id);

        // Verificar que la mascota exista y pertenezca al usuario
        if (mascota == null) {
            redirectAttributes.addFlashAttribute("error", "La mascota no existe");
            return "redirect:/mascotas";
        }

        if (!usuarioActual.isPresent() || !mascota.getPropietario().getIdUsuario().equals(usuarioActual.get().getIdUsuario())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para ver esta mascota");
            return "redirect:/mascotas";
        }

        model.addAttribute("mascota", mascota);
        return "mascotas/detallesMascota";
    }
}