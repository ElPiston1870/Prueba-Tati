package com.sistemaVeterinario.service;

import com.sistemaVeterinario.models.Role;
import com.sistemaVeterinario.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role obtenerRolUsuario() {
        return roleRepository.findByNombreRol("USER")
                .orElseThrow(() -> new IllegalStateException("El rol de usuario no existe"));
    }
}
