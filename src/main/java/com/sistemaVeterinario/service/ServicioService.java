package com.sistemaVeterinario.service;

import com.sistemaVeterinario.models.Servicio;
import com.sistemaVeterinario.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> findAllActive() {
        return servicioRepository.findByActivoTrue();
    }

    public Servicio findById(Integer id) {
        return servicioRepository.findById(id).orElse(null);
    }
}
