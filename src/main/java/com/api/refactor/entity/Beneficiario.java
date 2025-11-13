package com.api.refactor.entity;

import com.api.refactor.enums.TipoDonacion;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "beneficiarios")
@Data
public class Beneficiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String cedula;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private String telefono;

    private String email;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String ciudad;

    @Column(length = 500)
    private String descripcionSituacion;

    @ElementCollection(targetClass = TipoDonacion.class)
    @CollectionTable(name = "beneficiario_ayudas_necesarias", joinColumns = @JoinColumn(name = "beneficiario_id"))
    @Column(name = "tipo_ayuda")
    @Enumerated(EnumType.STRING)
    private Set<TipoDonacion> ayudasNecesarias;

    @Column(name = "personas_hogar")
    private Integer personasEnHogar;

    @Column(nullable = false)
    private String password;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo = true;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
