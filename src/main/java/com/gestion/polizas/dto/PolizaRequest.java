package com.gestion.polizas.dto;

import com.gestion.polizas.model.EstadoPoliza;
import com.gestion.polizas.model.TipoPoliza;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PolizaRequest {

    @NotNull(message = "El tipo de póliza es obligatorio")
    private TipoPoliza tipo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "El canon mensual es obligatorio")
    @PositiveOrZero(message = "El canon mensual debe ser mayor o igual a cero")
    private BigDecimal canonMensual;

    @NotNull(message = "Los meses de vigencia son obligatorios")
    @Positive(message = "Los meses de vigencia deben ser mayores a cero")
    private Integer mesesVigencia;

    private Long tomadorId;

    private Long beneficiarioId;
}