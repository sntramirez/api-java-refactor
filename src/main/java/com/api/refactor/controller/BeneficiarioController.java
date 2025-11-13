package com.api.refactor.controller;

import com.api.refactor.dto.request.BeneficiarioRegistroRequest;
import com.api.refactor.dto.response.BeneficiarioResponse;
import com.api.refactor.service.BeneficiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BeneficiarioController {

    private final BeneficiarioService beneficiarioService;

    @PostMapping("/registro")
    public ResponseEntity<BeneficiarioResponse> registrar(@Valid @RequestBody BeneficiarioRegistroRequest request) {
        BeneficiarioResponse response = beneficiarioService.registrarBeneficiario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BeneficiarioResponse>> obtenerTodos() {
        List<BeneficiarioResponse> beneficiarios = beneficiarioService.obtenerTodos();
        return ResponseEntity.ok(beneficiarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiarioResponse> obtenerPorId(@PathVariable Long id) {
        BeneficiarioResponse response = beneficiarioService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
}
