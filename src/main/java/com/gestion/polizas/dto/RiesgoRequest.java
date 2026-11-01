package com.gestion.polizas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiesgoRequest {

    @NotBlank(message = "La descripción del riesgo es obligatoria")
    private String descripcion;

    @NotNull(message = "El monto asegurado es obligatorio")
    @PositiveOrZero(message = "El monto asegurado debe ser mayor o igual a cero")
    private BigDecimal montoAsegurado;
}