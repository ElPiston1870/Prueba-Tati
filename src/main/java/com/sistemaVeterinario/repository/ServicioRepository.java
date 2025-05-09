package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByActivoTrue();
}