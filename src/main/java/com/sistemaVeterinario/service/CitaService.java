package com.sistemaVeterinario.service;

import com.sistemaVeterinario.models.Cita;
import com.sistemaVeterinario.models.Usuario;
import com.sistemaVeterinario.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public Cita save(Cita cita) {
        return citaRepository.save(cita);
    }

    public Cita findById(Integer id) {
        return citaRepository.findById(id).orElse(null);
    }

    public List<Cita> findByPropietario(Optional<Usuario> propietario) {
        return citaRepository.findByMascotaPropietario(propietario);
    }

    public List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha, Integer servicioId) {
        // Horarios posibles: 16 slots de 30 minutos desde las 9:00 hasta las 17:00
        List<LocalTime> todosLosHorarios = new ArrayList<>();
        for (int hora = 9; hora < 17; hora++) {
            todosLosHorarios.add(LocalTime.of(hora, 0));
            todosLosHorarios.add(LocalTime.of(hora, 30));
        }

        // Buscar citas ya programadas para la fecha
        LocalDateTime inicioDia = LocalDateTime.of(fecha, LocalTime.of(0, 0));
        LocalDateTime finDia = LocalDateTime.of(fecha, LocalTime.of(23, 59));

        List<Cita> citasProgramadas;
        if (servicioId != null) {
            citasProgramadas = citaRepository.findByFechaHoraBetweenAndServicioIdServicio(inicioDia, finDia, servicioId);
        } else {
            citasProgramadas = citaRepository.findByFechaHoraBetween(inicioDia, finDia);
        }

        // Extraer los horarios ya ocupados
        List<LocalTime> horariosOcupados = citasProgramadas.stream()
                .map(cita -> cita.getFechaHora().toLocalTime())
                .collect(Collectors.toList());

        // Filtrar horarios disponibles
        return todosLosHorarios.stream()
                .filter(horario -> !horariosOcupados.contains(horario))
                .collect(Collectors.toList());
    }
}