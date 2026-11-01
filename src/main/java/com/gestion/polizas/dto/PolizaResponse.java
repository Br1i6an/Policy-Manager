package com.gestion.polizas.dto;

import com.gestion.polizas.model.EstadoPoliza;
import com.gestion.polizas.model.TipoPoliza;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PolizaResponse {

    private Long id;
    private TipoPoliza tipo;
    private EstadoPoliza estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal canonMensual;
    private BigDecimal prima;
    private Integer mesesVigencia;
    private Long tomadorId;
    private Long beneficiarioId;
}