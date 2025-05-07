package com.sistemaVeterinario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    public @Pattern(regexp = "^(?!\\s)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,45})(?<!\\s)$", message = "{error.name.invalid}") String getNombre() {
        return nombre;
    }

    public void setNombre(@Pattern(regexp = "^(?!\\s)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,45})(?<!\\s)$", message = "{error.name.invalid}") String nombre) {
        this.nombre = nombre;
    }

    public @Pattern(regexp = "^(?!\\s)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,45})(?<!\\s)$", message = "{error.lastname.invalid}") String getApellido() {
        return apellido;
    }

    public void setApellido(@Pattern(regexp = "^(?!\\s)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,45})(?<!\\s)$", message = "{error.lastname.invalid}") String apellido) {
        this.apellido = apellido;
    }

    public @Email(message = "{error.email.invalid}") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "{error.email.invalid}") String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^3\\d{9}$", message = "{error.phone.invalid}") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@Pattern(regexp = "^3\\d{9}$", message = "{error.phone.invalid}") String telefono) {
        this.telefono = telefono;
    }

    public @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@_-]{8,}$", message = "{error.password.invalid}") String getContrasena() {
        return contrasena;
    }

    public void setContrasena(@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@_-]{8,}$", message = "{error.password.invalid}") String contrasena) {
        this.contrasena = contrasena;
    }

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