package com.sistemaVeterinario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "especialidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;

    @Column(name = "nombre", length = 45, nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "especialidad")
    private Set<VeterinarioInfo> veterinarios = new HashSet<>();
}