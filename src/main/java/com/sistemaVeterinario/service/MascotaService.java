package com.sistemaVeterinario.service;

import com.sistemaVeterinario.models.Mascota;
import com.sistemaVeterinario.models.Usuario;
import com.sistemaVeterinario.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<Mascota> findByPropietario(Optional<Usuario> propietario) {
        if (!propietario.isPresent()) {
            return new ArrayList<>();
        }
        // Ahora pasamos el Usuario directamente, no el Optional
        return mascotaRepository.findByPropietario(propietario.get());
    }

    public Mascota findById(Integer id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    public Mascota save(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    public void delete(Integer id) {
        mascotaRepository.deleteById(id);
    }

    public List<Mascota> findAll() {
        return mascotaRepository.findAll();
    }
}