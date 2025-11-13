package com.api.refactor.dto.response;

import com.api.refactor.enums.TipoDonacion;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BeneficiarioResponse {
    private Long id;
    private String nombre;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private String ciudad;
    private String descripcionSituacion;
    private Set<TipoDonacion> ayudasNecesarias;
    private Integer personasEnHogar;
    private Boolean activo;
}
