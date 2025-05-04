package com.sistemaVeterinario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "veterinarios_info")
public class VeterinarioInfo {

    @Id
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @Column(name = "activo")
    private Boolean activo = true;

    @OneToMany(mappedBy = "veterinario")
    private Set<Cita> citas = new HashSet<>();
}