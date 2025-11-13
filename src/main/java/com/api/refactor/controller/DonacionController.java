package com.api.refactor.controller;

import com.api.refactor.dto.request.DonacionRequest;
import com.api.refactor.dto.response.DonacionResponse;
import com.api.refactor.enums.EstadoDonacion;
import com.api.refactor.service.DonacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonacionController {

    private final DonacionService donacionService;

    @PostMapping
    public ResponseEntity<DonacionResponse> crearDonacion(@Valid @RequestBody DonacionRequest request) {
        DonacionResponse response = donacionService.crearDonacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DonacionResponse>> obtenerPorEstado(@PathVariable EstadoDonacion estado) {
        List<DonacionResponse> donaciones = donacionService.obtenerDonacionesPorEstado(estado);
        return ResponseEntity.ok(donaciones);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<DonacionResponse>> obtenerDisponibles() {
        List<DonacionResponse> donaciones = donacionService.obtenerDonacionesDisponibles();
        return ResponseEntity.ok(donaciones);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<DonacionResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        EstadoDonacion nuevoEstado = EstadoDonacion.valueOf(body.get("estado"));
        DonacionResponse response = donacionService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }
}
