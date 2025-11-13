package com.api.refactor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignacionRequest {

    @NotNull(message = "El ID de la donación es requerido")
    private Long donacionId;

    @NotNull(message = "El ID del beneficiario es requerido")
    private Long beneficiarioId;

    private Long solicitudId;

    private String comentarios;
}
