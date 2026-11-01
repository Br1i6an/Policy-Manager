package com.gestion.polizas.controller;

import com.gestion.polizas.dto.PolizaRequest;
import com.gestion.polizas.dto.PolizaResponse;
import com.gestion.polizas.service.PolizaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
public class PolizaController {

    private final PolizaService polizaService;

    @GetMapping
    public ResponseEntity<List<PolizaResponse>> listarPolizas(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(polizaService.listarPolizas(tipo, estado));
    }

    @PostMapping
    public ResponseEntity<PolizaResponse> crearPoliza(@RequestBody @Valid PolizaRequest request) {
        PolizaResponse respuesta = polizaService.crearPoliza(request);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolizaResponse> obtenerPoliza(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.obtenerPoliza(id));
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<PolizaResponse> renovar(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.renovarPoliza(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        polizaService.cancelarPoliza(id);
        return ResponseEntity.noContent().build();
    }

}