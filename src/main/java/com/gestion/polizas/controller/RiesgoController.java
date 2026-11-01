package com.gestion.polizas.controller;

import com.gestion.polizas.dto.RiesgoRequest;
import com.gestion.polizas.dto.RiesgoResponse;
import com.gestion.polizas.service.RiesgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RiesgoController {

    private final RiesgoService riesgoService;

    @GetMapping("/polizas/{id}/riesgos")
    public ResponseEntity<List<RiesgoResponse>> listarRiesgos(@PathVariable Long id) {
        return ResponseEntity.ok(riesgoService.listarRiesgosPorPoliza(id));
    }

    @PostMapping("/polizas/{id}/riesgos")
    public ResponseEntity<RiesgoResponse> agregarRiesgo(@PathVariable Long id,
                                                        @RequestBody @Valid RiesgoRequest request) {
        RiesgoResponse respuesta = riesgoService.agregarRiesgo(id, request);
        return ResponseEntity.created(URI.create("/api/v1/riesgos/" + respuesta.getId())).body(respuesta);
    }

    @PostMapping("/riesgos/{id}/cancelar")
    public ResponseEntity<Void> cancelarRiesgo(@PathVariable Long id) {
        riesgoService.cancelarRiesgo(id);
        return ResponseEntity.noContent().build();
    }
}