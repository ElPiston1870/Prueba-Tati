package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.Cita;
import com.sistemaVeterinario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByMascotaPropietario(Optional<Usuario> propietario);
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByFechaHoraBetweenAndServicioIdServicio(LocalDateTime inicio, LocalDateTime fin, Integer servicioId);
    List<Cita> findByFechaHora(LocalDateTime fechaHora);
    List<Cita> findByEstadoAndFechaHoraBefore(Cita.EstadoCita estado, LocalDateTime fechaHora);

    @Modifying
    @Query("UPDATE Cita c SET c.estado = :nuevoEstado WHERE c.estado = :estadoActual AND c.fechaHora < :fechaReferencia")
    int actualizarCitasPasadas(@Param("nuevoEstado") Cita.EstadoCita nuevoEstado,
                               @Param("estadoActual") Cita.EstadoCita estadoActual,
                               @Param("fechaReferencia") LocalDateTime fechaReferencia);
}