package com.gestion.polizas.controller;

import com.gestion.polizas.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/core-mock")
@RequiredArgsConstructor
public class CoreMockController {

    private final NotificacionService notificacionService;

    @PostMapping("/evento")
    public ResponseEntity<Void> recibirEvento(@RequestBody Map<String, Object> evento) {
        String eventoTipo = (String) evento.get("evento");
        Object polizaIdObj = evento.get("polizaId");
        Long polizaId = polizaIdObj != null ? Long.valueOf(polizaIdObj.toString()) : null;

        log.info("[CORE-MOCK] Evento recibido: evento={}, polizaId={}", eventoTipo, polizaId);
        notificacionService.enviarEventoCore(eventoTipo != null ? eventoTipo : "DESCONOCIDO", polizaId);

        return ResponseEntity.ok().build();
    }
}