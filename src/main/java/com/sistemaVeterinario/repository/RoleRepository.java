package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNombreRol(String nombreRol);

}
