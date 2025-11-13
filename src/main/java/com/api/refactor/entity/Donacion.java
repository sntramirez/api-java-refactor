package com.api.refactor.entity;

import com.api.refactor.enums.EstadoDonacion;
import com.api.refactor.enums.TipoDonacion;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "donaciones")
@Data
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDonacion tipo;

    @Column(nullable = false)
    private String cantidad;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "donante_id", nullable = false)
    private Donante donante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDonacion estado = EstadoDonacion.NUEVA;

    @Column(name = "fecha_donacion")
    private LocalDateTime fechaDonacion;

    @Column(name = "numero_referencia", unique = true)
    private String numeroReferencia;

    @Column(name = "comentarios_admin")
    private String comentariosAdmin;

    @PrePersist
    protected void onCreate() {
        fechaDonacion = LocalDateTime.now();
        numeroReferencia = "DON-" + System.currentTimeMillis();
    }
}
