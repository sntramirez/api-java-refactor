package com.api.refactor.dto.request;

import com.api.refactor.enums.TipoDonacion;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BeneficiarioRegistroRequest {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La cédula es requerida")
    private String cedula;

    @NotNull(message = "La fecha de nacimiento es requerida")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El teléfono es requerido")
    private String telefono;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "La dirección es requerida")
    private String direccion;

    @NotBlank(message = "La ciudad es requerida")
    private String ciudad;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcionSituacion;

    @NotEmpty(message = "Debe seleccionar al menos un tipo de ayuda")
    private Set<TipoDonacion> ayudasNecesarias;

    @Positive(message = "El número de personas debe ser positivo")
    private Integer personasEnHogar;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}
