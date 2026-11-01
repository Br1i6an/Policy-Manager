package com.gestion.polizas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "riesgos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "poliza_id", nullable = false)
    private Long polizaId;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRiesgo estado;

    @Column(name = "monto_asegurado", precision = 12, scale = 2)
    private BigDecimal montoAsegurado;
}