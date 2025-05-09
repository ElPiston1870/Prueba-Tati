package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.Mascota;
import com.sistemaVeterinario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    // Este método espera un Usuario, no un Optional<Usuario>
    List<Mascota> findByPropietario(Usuario propietario);
}