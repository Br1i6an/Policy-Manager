package com.gestion.polizas.service;

import com.gestion.polizas.dto.PolizaRequest;
import com.gestion.polizas.dto.PolizaResponse;
import com.gestion.polizas.exception.BusinessException;
import com.gestion.polizas.model.EstadoPoliza;
import com.gestion.polizas.model.EstadoRiesgo;
import com.gestion.polizas.model.TipoPoliza;
import com.gestion.polizas.model.Poliza;
import com.gestion.polizas.model.Riesgo;
import com.gestion.polizas.repository.PolizaRepository;
import com.gestion.polizas.repository.RiesgoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolizaService {

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final NotificacionService notificacionService;

    @Value("${poliza.ipc.porcentaje:5.0}")
    private BigDecimal ipcPorcentaje;

    public List<PolizaResponse> listarPolizas(String tipo, String estado) {
        List<Poliza> polizas;

        if (tipo != null && estado != null) {
            polizas = polizaRepository.findByTipoAndEstado(
                    TipoPoliza.valueOf(tipo.toUpperCase()),
                    EstadoPoliza.valueOf(estado.toUpperCase())
            );
        } else if (tipo != null) {
            polizas = polizaRepository.findByTipo(TipoPoliza.valueOf(tipo.toUpperCase()));
        } else if (estado != null) {
            polizas = polizaRepository.findByEstado(EstadoPoliza.valueOf(estado.toUpperCase()));
        } else {
            polizas = polizaRepository.findAll();
        }

        return polizas.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public PolizaResponse crearPoliza(PolizaRequest request) {
        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        Integer mesesVigencia = request.getMesesVigencia();
        if (mesesVigencia == null) {
            mesesVigencia = (int) java.time.temporal.ChronoUnit.MONTHS.between(request.getFechaInicio(), request.getFechaFin());
            if (mesesVigencia <= 0) {
                mesesVigencia = 1;
            }
        }

        BigDecimal prima = request.getCanonMensual().multiply(BigDecimal.valueOf(mesesVigencia));

        Poliza poliza = Poliza.builder()
                .tipo(request.getTipo())
                .estado(EstadoPoliza.ACTIVA)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .canonMensual(request.getCanonMensual())
                .prima(prima)
                .mesesVigencia(mesesVigencia)
                .tomadorId(request.getTomadorId())
                .beneficiarioId(request.getBeneficiarioId())
                .build();

        Poliza polizaGuardada = polizaRepository.save(poliza);

        notificacionService.enviarNotificacionCreacion(polizaGuardada.getId());

        notificacionService.enviarEventoCore("ACTUALIZACION", polizaGuardada.getId());

        log.info("Poliza creada con id: {}", polizaGuardada.getId());

        return toResponse(polizaGuardada);
    }

    public PolizaResponse obtenerPoliza(Long id) {
        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Poliza no encontrada con id: " + id));
        return toResponse(poliza);
    }

    @Transactional
    public PolizaResponse renovarPoliza(Long id) {
        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Poliza no encontrada con id: " + id));

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException("No se puede renovar una poliza cancelada");
        }

        BigDecimal incremento = poliza.getCanonMensual()
                .multiply(ipcPorcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        BigDecimal nuevoCanon = poliza.getCanonMensual().add(incremento);

        BigDecimal nuevaPrima = nuevoCanon.multiply(BigDecimal.valueOf(poliza.getMesesVigencia()));

        poliza.setCanonMensual(nuevoCanon.setScale(2, RoundingMode.HALF_UP));
        poliza.setPrima(nuevaPrima.setScale(2, RoundingMode.HALF_UP));
        poliza.setEstado(EstadoPoliza.RENOVADA);

        Poliza polizaGuardada = polizaRepository.save(poliza);

        notificacionService.enviarNotificacionRenovacion(polizaGuardada.getId());

        notificacionService.enviarEventoCore("ACTUALIZACION", polizaGuardada.getId());

        log.info("Poliza {} renovada. Nuevo canon: {}, Nueva prima: {}", id, nuevoCanon, nuevaPrima);

        return toResponse(polizaGuardada);
    }

    @Transactional
    public void cancelarPoliza(Long id) {
        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Poliza no encontrada con id: " + id));

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException("La poliza ya esta cancelada");
        }

        poliza.setEstado(EstadoPoliza.CANCELADA);
        polizaRepository.save(poliza);

        List<Riesgo> riesgos = riesgoRepository.findByPolizaId(id);
        for (Riesgo riesgo : riesgos) {
            riesgo.setEstado(EstadoRiesgo.CANCELADO);
            riesgoRepository.save(riesgo);
        }

        notificacionService.enviarNotificacionCancelacion(id);

        notificacionService.enviarEventoCore("ACTUALIZACION", id);

        log.info("Poliza {} cancelada junto con sus {} riesgo(s)", id, riesgos.size());
    }

    private PolizaResponse toResponse(Poliza poliza) {
        PolizaResponse response = new PolizaResponse();
        response.setId(poliza.getId());
        response.setTipo(poliza.getTipo());
        response.setEstado(poliza.getEstado());
        response.setFechaInicio(poliza.getFechaInicio());
        response.setFechaFin(poliza.getFechaFin());
        response.setCanonMensual(poliza.getCanonMensual());
        response.setPrima(poliza.getPrima());
        response.setMesesVigencia(poliza.getMesesVigencia());
        response.setTomadorId(poliza.getTomadorId());
        response.setBeneficiarioId(poliza.getBeneficiarioId());
        return response;
    }
}