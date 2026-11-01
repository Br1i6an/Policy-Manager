package com.gestion.polizas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "polizas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPoliza tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPoliza estado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "canon_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal canonMensual;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prima;

    @Column(name = "meses_vigencia", nullable = false)
    private Integer mesesVigencia;

    @Column(name = "tomador_id")
    private Long tomadorId;

    @Column(name = "beneficiario_id")
    private Long beneficiarioId;
}