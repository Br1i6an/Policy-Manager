package com.gestion.polizas.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacionService {

    public void enviarNotificacionCreacion(Long polizaId) {
        log.info("[NOTIFICACION] Póliza {} creada. Enviando correo/SMS al tomador y beneficiario.", polizaId);
    }

    public void enviarNotificacionRenovacion(Long polizaId) {
        log.info("[NOTIFICACION] Póliza {} renovada. Enviando correo/SMS de renovación.", polizaId);
    }

    public void enviarNotificacionCancelacion(Long polizaId) {
        log.info("[NOTIFICACION] Póliza {} cancelada. Enviando correo/SMS de cancelación.", polizaId);
    }

    public void enviarNotificacionNuevoRiesgo(Long polizaId, Long riesgoId) {
        log.info("[NOTIFICACION] Riesgo {} agregado a la póliza {}. Enviando correo/SMS.", riesgoId, polizaId);
    }

    public void enviarEventoCore(String evento, Long polizaId) {
        log.info("[CORE-MOCK] Evento enviado al CORE: evento={}, polizaId={}", evento, polizaId);
    }
}