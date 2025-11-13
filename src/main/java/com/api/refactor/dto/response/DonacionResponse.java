package com.api.refactor.dto.response;

import com.api.refactor.enums.EstadoDonacion;
import com.api.refactor.enums.TipoDonacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonacionResponse {
    private Long id;
    private TipoDonacion tipo;
    private String cantidad;
    private String descripcion;
    private String nombreDonante;
    private EstadoDonacion estado;
    private LocalDateTime fechaDonacion;
    private String numeroReferencia;
}
