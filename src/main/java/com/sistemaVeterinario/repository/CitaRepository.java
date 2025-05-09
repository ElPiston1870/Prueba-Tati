package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.Cita;
import com.sistemaVeterinario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByMascotaPropietario(Optional<Usuario> propietario);
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByFechaHoraBetweenAndServicioIdServicio(LocalDateTime inicio, LocalDateTime fin, Integer servicioId);
    List<Cita> findByFechaHora(LocalDateTime fechaHora);
}