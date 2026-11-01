package com.gestion.polizas.dto;

import com.gestion.polizas.model.EstadoRiesgo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiesgoResponse {

    private Long id;
    private Long polizaId;
    private String descripcion;
    private EstadoRiesgo estado;
    private BigDecimal montoAsegurado;
}