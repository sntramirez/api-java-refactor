package com.api.refactor.dto.response;

import com.api.refactor.enums.EstadoAsignacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AsignacionResponse {
    private Long id;
    private DonacionResponse donacion;
    private BeneficiarioResponse beneficiario;
    private EstadoAsignacion estado;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaActualizacion;
    private String comentarios;
}
