package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.VeterinarioInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinarioInfoRepository extends JpaRepository<VeterinarioInfo, Integer> {
    List<VeterinarioInfo> findByServicioIdServicioAndActivoTrue(Integer servicioId);
    List<VeterinarioInfo> findByActivoTrue();
}