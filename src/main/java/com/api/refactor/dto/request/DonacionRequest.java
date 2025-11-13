package com.api.refactor.dto.request;

import com.api.refactor.enums.TipoDonacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DonacionRequest {

    @NotNull(message = "El tipo de donación es requerido")
    private TipoDonacion tipo;

    @NotBlank(message = "La cantidad es requerida")
    private String cantidad;

    private String descripcion;

    @NotBlank(message = "El nombre del donante es requerido")
    private String nombreDonante;

    @NotBlank(message = "El teléfono es requerido")
    private String telefonoDonante;

    @NotBlank(message = "El email es requerido")
    private String emailDonante;

    private String comentarios;
}
