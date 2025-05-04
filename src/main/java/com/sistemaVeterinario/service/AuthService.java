package com.sistemaVeterinario.service;

import com.sistemaVeterinario.dto.UsuarioDTO;
import com.sistemaVeterinario.models.Role;
import com.sistemaVeterinario.models.Usuario;
import com.sistemaVeterinario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final MessageSource messageSource;

    @Autowired
    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RoleService roleService,
            MessageSource messageSource) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("error.user.notFound",
                                new Object[]{email},
                                LocaleContextHolder.getLocale())));

        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombreRol()))
                .collect(Collectors.toList());

        return new User(usuario.getEmail(), usuario.getContrasena(), authorities);
    }

    @Transactional
    public Usuario registrarUsuario(UsuarioDTO registroDTO) {
        // Verifica si el email ya existe
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("error.email.existing",
                            null,
                            LocaleContextHolder.getLocale()));
        }

        // Verificar si el teléfono ya existe
        if (usuarioRepository.existsByTelefono(registroDTO.getTelefono())) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("error.phone.existing",
                            null,
                            LocaleContextHolder.getLocale()));
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setTelefono(registroDTO.getTelefono());

        // Encriptar la contraseña
        usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));

        // Asignar rol de usuario por defecto
        Role rolUsuario = roleService.obtenerRolUsuario();
        usuario.setRoles(new HashSet<>(Collections.singletonList(rolUsuario)));

        // Guardar en la base de datos
        return usuarioRepository.save(usuario);
    }
}