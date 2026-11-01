package com.gestion.polizas.service;

import com.gestion.polizas.dto.RiesgoRequest;
import com.gestion.polizas.dto.RiesgoResponse;
import com.gestion.polizas.exception.BusinessException;
import com.gestion.polizas.model.EstadoPoliza;
import com.gestion.polizas.model.EstadoRiesgo;
import com.gestion.polizas.model.Poliza;
import com.gestion.polizas.model.Riesgo;
import com.gestion.polizas.repository.PolizaRepository;
import com.gestion.polizas.repository.RiesgoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiesgoService {

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final NotificacionService notificacionService;

    public List<RiesgoResponse> listarRiesgosPorPoliza(Long polizaId) {
        polizaRepository.findById(polizaId)
                .orElseThrow(() -> new BusinessException("Poliza no encontrada con id: " + polizaId));

        List<Riesgo> riesgos = riesgoRepository.findByPolizaId(polizaId);
        return riesgos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public RiesgoResponse agregarRiesgo(Long polizaId, RiesgoRequest request) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new BusinessException("Poliza no encontrada con id: " + polizaId));

        if (poliza.getTipo() != com.gestion.polizas.model.TipoPoliza.COLECTIVA) {
            throw new BusinessException("Solo las pólizas Colectivas pueden tener riesgos asociados");
        }

        if (poliza.getTipo() == com.gestion.polizas.model.TipoPoliza.INDIVIDUAL) {
            long cantidadRiesgos = riesgoRepository.countByPolizaId(polizaId);
            if (cantidadRiesgos >= 1) {
                throw new BusinessException("Una póliza Individual solo puede tener 1 riesgo");
            }
        }

        Riesgo riesgo = Riesgo.builder()
                .polizaId(polizaId)
                .descripcion(request.getDescripcion())
                .estado(EstadoRiesgo.ACTIVO)
                .montoAsegurado(request.getMontoAsegurado())
                .build();

        Riesgo riesgoGuardado = riesgoRepository.save(riesgo);

        notificacionService.enviarNotificacionNuevoRiesgo(polizaId, riesgoGuardado.getId());

        notificacionService.enviarEventoCore("ACTUALIZACION", polizaId);

        log.info("Riesgo {} agregado a la poliza {}", riesgoGuardado.getId(), polizaId);

        return toResponse(riesgoGuardado);
    }

    @Transactional
    public void cancelarRiesgo(Long id) {
        Riesgo riesgo = riesgoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Riesgo no encontrado con id: " + id));

        if (riesgo.getEstado() == EstadoRiesgo.CANCELADO) {
            throw new BusinessException("El riesgo ya esta cancelado");
        }

        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        riesgoRepository.save(riesgo);

        notificacionService.enviarEventoCore("ACTUALIZACION", riesgo.getPolizaId());

        log.info("Riesgo {} cancelado (poliza: {})", id, riesgo.getPolizaId());
    }

    private RiesgoResponse toResponse(Riesgo riesgo) {
        RiesgoResponse response = new RiesgoResponse();
        response.setId(riesgo.getId());
        response.setPolizaId(riesgo.getPolizaId());
        response.setDescripcion(riesgo.getDescripcion());
        response.setEstado(riesgo.getEstado());
        response.setMontoAsegurado(riesgo.getMontoAsegurado());
        return response;
    }
}