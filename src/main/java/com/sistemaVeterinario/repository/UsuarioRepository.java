package com.sistemaVeterinario.repository;

import com.sistemaVeterinario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
    /*Permite encontrar un usuario por su email*/
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
}
