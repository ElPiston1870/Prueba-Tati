package com.sistemaVeterinario.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 10, nullable = false, unique = true)
    private String telefono;

    @Column(length = 255, nullable = false)
    private String contrasena;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL)
    private Set<Mascota> mascotas = new HashSet<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private VeterinarioInfo veterinarioInfo;

    @PrePersist
    public void prePersist(){
        fechaRegistro = LocalDateTime.now();
    }
}

