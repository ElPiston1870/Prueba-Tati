package com.sistemaVeterinario.service;


import com.sistemaVeterinario.models.Cita;
import com.sistemaVeterinario.models.VeterinarioInfo;
import com.sistemaVeterinario.repository.CitaRepository;
import com.sistemaVeterinario.repository.VeterinarioInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioInfoRepository veterinarioInfoRepository;

    @Autowired
    private CitaRepository citaRepository;

    public VeterinarioInfo encontrarVeterinarioDisponible(Integer servicioId, LocalDateTime fechaHora) {
        // Buscar veterinarios que ofrezcan el servicio y estén activos
        List<VeterinarioInfo> veterinarios = veterinarioInfoRepository.findByServicioIdServicioAndActivoTrue(servicioId);

        if (veterinarios.isEmpty()) {
            // Si no hay veterinarios para ese servicio, buscar cualquier veterinario activo
            veterinarios = veterinarioInfoRepository.findByActivoTrue();
            if (veterinarios.isEmpty()) {
                return null; // No hay veterinarios disponibles
            }
        }

        // Buscar veterinarios que no tengan cita a esa hora
        List<Cita> citasExistentes = citaRepository.findByFechaHora(fechaHora);
        List<Integer> veterinariosOcupados = citasExistentes.stream()
                .filter(c -> c.getVeterinario() != null)
                .map(c -> c.getVeterinario().getUsuarioId())
                .collect(Collectors.toList());

        // Filtrar veterinarios disponibles
        List<VeterinarioInfo> veterinariosDisponibles = veterinarios.stream()
                .filter(v -> !veterinariosOcupados.contains(v.getUsuarioId()))
                .collect(Collectors.toList());

        // Si hay veterinarios disponibles, retornar el primero
        if (!veterinariosDisponibles.isEmpty()) {
            return veterinariosDisponibles.get(0);
        }

        // Si todos están ocupados, retornar el primero de la lista general
        return veterinarios.get(0);
    }
}