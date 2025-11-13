package com.api.refactor.entity;

import com.api.refactor.enums.EstadoSolicitud;
import com.api.refactor.enums.TipoDonacion;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
@Data
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "beneficiario_id", nullable = false)
    private Beneficiario beneficiario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDonacion tipoAyuda;

    @Column(length = 500)
    private String comentarios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "comentarios_admin")
    private String comentariosAdmin;

    @PrePersist
    protected void onCreate() {
        fechaSolicitud = LocalDateTime.now();
    }
}
