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
    List<Cita> findByFechaHora(LocalDateTime fechaHora);
    List<Cita> findByEstadoAndFechaHoraBefore(Cita.EstadoCita estado, LocalDateTime fechaHora);

    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :inicio AND :fin AND c.estado != 'Cancelada'")
    List<Cita> findByFechaHoraBetween(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    // Método para buscar citas entre fechas por servicio excluyendo canceladas
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :inicio AND :fin " +
            "AND c.servicio.idServicio = :servicioId AND c.estado != 'Cancelada'")
    List<Cita>  findByFechaHoraBetweenAndServicioIdServicio(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("servicioId") Integer servicioId);

    @Modifying
    @Query("UPDATE Cita c SET c.estado = :nuevoEstado WHERE c.estado = :estadoActual AND c.fechaHora < :fechaReferencia")
    int actualizarCitasPasadas(@Param("nuevoEstado") Cita.EstadoCita nuevoEstado,
                               @Param("estadoActual") Cita.EstadoCita estadoActual,
                               @Param("fechaReferencia") LocalDateTime fechaReferencia);
}
