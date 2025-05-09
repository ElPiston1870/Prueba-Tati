package com.sistemaVeterinario.controller;

import com.sistemaVeterinario.models.*;
import com.sistemaVeterinario.repository.*;
import com.sistemaVeterinario.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/citas")
public class AppointmentController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private VeterinarioService veterinarioService;

    @Autowired
    private CitaService citaService;

    @GetMapping("/agendar")
    public String mostrarFormularioCita(Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        // Obtener mascotas del usuario
        List<Mascota> mascotas = mascotaService.findByPropietario(usuarioActual);

        // Obtener servicios disponibles
        List<Servicio> servicios = servicioService.findAllActive();

        // Crear lista de fechas disponibles (2 semanas desde mañana)
        List<LocalDate> fechasDisponibles = generarFechasDisponibles();

        model.addAttribute("mascotas", mascotas);
        model.addAttribute("servicios", servicios);
        model.addAttribute("fechasDisponibles", fechasDisponibles);
        model.addAttribute("nuevaCita", new Cita());

        return "citas/agendar";
    }

    @GetMapping("/horarios/{fecha}")
    @ResponseBody
    public Map<String, Object> obtenerHorariosDisponibles(
            @PathVariable("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Integer servicioId) {

        // Obtener horarios disponibles para la fecha seleccionada
        List<LocalTime> horariosDisponibles = citaService.obtenerHorariosDisponibles(fecha, servicioId);

        // Formatear horas para mostrar en la interfaz
        List<String> horasFormateadas = new ArrayList<>();
        Map<String, Boolean> disponibilidad = new HashMap<>();

        for (int i = 9; i < 17; i++) { // 9am a 5pm
            for (int m = 0; m < 60; m += 30) { // Intervalos de 30 minutos
                LocalTime hora = LocalTime.of(i, m);
                String horaStr = hora.toString();
                horasFormateadas.add(horaStr);
                disponibilidad.put(horaStr, horariosDisponibles.contains(hora));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("horas", horasFormateadas);
        result.put("disponibilidad", disponibilidad);

        return result;
    }

    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute("nuevaCita") Cita cita,
                              @RequestParam("fechaSeleccionada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                              @RequestParam("horaSeleccionada") String hora,
                              @RequestParam("mascotaId") Integer mascotaId,
                              @RequestParam("servicioId") Integer servicioId) {

        // Construir fecha y hora completa
        String[] partesHora = hora.split(":");
        LocalTime horaLocal = LocalTime.of(Integer.parseInt(partesHora[0]), Integer.parseInt(partesHora[1]));
        LocalDateTime fechaHora = LocalDateTime.of(fecha, horaLocal);

        // Buscar mascota y servicio
        Mascota mascota = mascotaService.findById(mascotaId);
        Servicio servicio = servicioService.findById(servicioId);

        // Encontrar veterinario disponible para el servicio
        VeterinarioInfo veterinario = veterinarioService.encontrarVeterinarioDisponible(servicioId, fechaHora);

        // Configurar cita
        cita.setMascota(mascota);
        cita.setServicio(servicio);
        cita.setVeterinario(veterinario);
        cita.setFechaHora(fechaHora);
        cita.setEstado(Cita.EstadoCita.Programada);

        // Guardar cita
        citaService.save(cita);

        return "redirect:/citas/mis-citas?exito";
    }

    @GetMapping("/mis-citas")
    public String misCitas(Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        // Obtener citas del usuario a través de sus mascotas
        List<Cita> citas = citaService.findByPropietario(usuarioActual);

        model.addAttribute("citas", citas);

        return "citas/mis-citas";
    }

    private List<LocalDate> generarFechasDisponibles() {
        List<LocalDate> fechas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate manana = hoy.plusDays(1);

        // Generar 14 días desde mañana
        for (int i = 0; i < 14; i++) {
            fechas.add(manana.plusDays(i));
        }

        return fechas;
    }
    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable("id") Integer idCita) {
        // Verificar que la cita pertenezca al usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        Cita cita = citaService.findById(idCita);

        // Verificar que la cita exista y pertenezca al usuario
        if (cita != null && usuarioActual.isPresent() &&
                cita.getMascota().getPropietario().getIdUsuario().equals(usuarioActual.get().getIdUsuario())) {
            // Cambiar estado a cancelada
            cita.setEstado(Cita.EstadoCita.Cancelada);
            citaService.save(cita);
            return "redirect:/citas/mis-citas?cancelada";
        }

        return "redirect:/citas/mis-citas?error";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer idCita, Model model) {
        // Verificar que la cita pertenezca al usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        Cita cita = citaService.findById(idCita);

        // Verificar que la cita exista y pertenezca al usuario
        if (cita == null || !usuarioActual.isPresent() ||
                !cita.getMascota().getPropietario().getIdUsuario().equals(usuarioActual.get().getIdUsuario())) {
            return "redirect:/citas/mis-citas?error";
        }

        // Si la cita no está programada, no se puede editar
        if (cita.getEstado() != Cita.EstadoCita.Programada) {
            return "redirect:/citas/mis-citas?noeditable";
        }

        // Obtener mascotas del usuario
        List<Mascota> mascotas = mascotaService.findByPropietario(usuarioActual);

        // Obtener servicios disponibles
        List<Servicio> servicios = servicioService.findAllActive();

        // Crear lista de fechas disponibles (2 semanas desde mañana)
        List<LocalDate> fechasDisponibles = generarFechasDisponibles();

        model.addAttribute("mascotas", mascotas);
        model.addAttribute("servicios", servicios);
        model.addAttribute("fechasDisponibles", fechasDisponibles);
        model.addAttribute("cita", cita);

        return "citas/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCita(@PathVariable("id") Integer idCita,
                                 @RequestParam("fechaSeleccionada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                 @RequestParam("horaSeleccionada") String hora,
                                 @RequestParam("mascotaId") Integer mascotaId,
                                 @RequestParam("servicioId") Integer servicioId) {

        // Verificar que la cita pertenezca al usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(email);

        Cita cita = citaService.findById(idCita);

        // Verificar que la cita exista y pertenezca al usuario
        if (cita == null || !usuarioActual.isPresent() ||
                !cita.getMascota().getPropietario().getIdUsuario().equals(usuarioActual.get().getIdUsuario())) {
            return "redirect:/citas/mis-citas?error";
        }

        // Si la cita no está programada, no se puede editar
        if (cita.getEstado() != Cita.EstadoCita.Programada) {
            return "redirect:/citas/mis-citas?noeditable";
        }

        // Construir fecha y hora completa
        String[] partesHora = hora.split(":");
        LocalTime horaLocal = LocalTime.of(Integer.parseInt(partesHora[0]), Integer.parseInt(partesHora[1]));
        LocalDateTime fechaHora = LocalDateTime.of(fecha, horaLocal);

        // Buscar mascota y servicio
        Mascota mascota = mascotaService.findById(mascotaId);
        Servicio servicio = servicioService.findById(servicioId);

        // Encontrar veterinario disponible para el servicio
        VeterinarioInfo veterinario = veterinarioService.encontrarVeterinarioDisponible(servicioId, fechaHora);

        // Configurar cita
        cita.setMascota(mascota);
        cita.setServicio(servicio);
        cita.setVeterinario(veterinario);
        cita.setFechaHora(fechaHora);

        // Guardar cita
        citaService.save(cita);

        return "redirect:/citas/mis-citas?actualizada";
    }
}
