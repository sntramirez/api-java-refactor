package com.api.refactor.controller;

import com.api.refactor.dto.request.AsignacionRequest;
import com.api.refactor.dto.response.AsignacionResponse;
import com.api.refactor.enums.EstadoAsignacion;
import com.api.refactor.service.AsignacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AsignacionController {

    private final AsignacionService asignacionService;

    @PostMapping
    public ResponseEntity<AsignacionResponse> crearAsignacion(@Valid @RequestBody AsignacionRequest request) {
        AsignacionResponse response = asignacionService.crearAsignacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/beneficiario/{beneficiarioId}")
    public ResponseEntity<List<AsignacionResponse>> obtenerPorBeneficiario(@PathVariable Long beneficiarioId) {
        List<AsignacionResponse> asignaciones = asignacionService.obtenerPorBeneficiario(beneficiarioId);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AsignacionResponse>> obtenerPorEstado(@PathVariable EstadoAsignacion estado) {
        List<AsignacionResponse> asignaciones = asignacionService.obtenerPorEstado(estado);
        return ResponseEntity.ok(asignaciones);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<AsignacionResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        EstadoAsignacion nuevoEstado = EstadoAsignacion.valueOf(body.get("estado"));
        String comentarios = body.get("comentarios");
        AsignacionResponse response = asignacionService.actualizarEstado(id, nuevoEstado, comentarios);
        return ResponseEntity.ok(response);
    }
}
