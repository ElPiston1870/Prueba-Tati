package com.sistemaVeterinario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    @Pattern(regexp = "^(?!\\s)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,45})(?<!\\s)$", message = "{error.name.invalid}")
    private String nombre;

    @Pattern(regexp = "^(?!\\s)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,45})(?<!\\s)$", message = "{error.lastname.invalid}")
    private String apellido;

    @Email(message="{error.email.invalid}")
    private String email;

    @Pattern(regexp = "^3\\d{9}$", message = "{error.phone.invalid}")
    private String telefono;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@_-]{8,}$", message = "{error.password.invalid}")
    private String contrasena;
}