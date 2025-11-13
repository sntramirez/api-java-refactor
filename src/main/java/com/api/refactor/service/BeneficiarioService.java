package com.api.refactor.service;

import com.api.refactor.dto.request.BeneficiarioRegistroRequest;
import com.api.refactor.dto.response.BeneficiarioResponse;
import com.api.refactor.entity.Beneficiario;
import com.api.refactor.repository.BeneficiarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiarioService {

    private final BeneficiarioRepository beneficiarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public BeneficiarioResponse registrarBeneficiario(BeneficiarioRegistroRequest request) {
        if (beneficiarioRepository.existsByCedula(request.getCedula())) {
            throw new RuntimeException("Ya existe un beneficiario con esta cédula");
        }

        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setNombre(request.getNombre());
        beneficiario.setCedula(request.getCedula());
        beneficiario.setFechaNacimiento(request.getFechaNacimiento());
        beneficiario.setTelefono(request.getTelefono());
        beneficiario.setEmail(request.getEmail());
        beneficiario.setDireccion(request.getDireccion());
        beneficiario.setCiudad(request.getCiudad());
        beneficiario.setDescripcionSituacion(request.getDescripcionSituacion());
        beneficiario.setAyudasNecesarias(request.getAyudasNecesarias());
        beneficiario.setPersonasEnHogar(request.getPersonasEnHogar());
        beneficiario.setPassword(passwordEncoder.encode(request.getPassword()));

        beneficiario = beneficiarioRepository.save(beneficiario);

        return mapToResponse(beneficiario);
    }

    public List<BeneficiarioResponse> obtenerTodos() {
        return beneficiarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BeneficiarioResponse obtenerPorId(Long id) {
        Beneficiario beneficiario = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiario no encontrado"));
        return mapToResponse(beneficiario);
    }

    private BeneficiarioResponse mapToResponse(Beneficiario beneficiario) {
        BeneficiarioResponse response = new BeneficiarioResponse();
        response.setId(beneficiario.getId());
        response.setNombre(beneficiario.getNombre());
        response.setCedula(beneficiario.getCedula());
        response.setFechaNacimiento(beneficiario.getFechaNacimiento());
        response.setTelefono(beneficiario.getTelefono());
        response.setEmail(beneficiario.getEmail());
        response.setDireccion(beneficiario.getDireccion());
        response.setCiudad(beneficiario.getCiudad());
        response.setDescripcionSituacion(beneficiario.getDescripcionSituacion());
        response.setAyudasNecesarias(beneficiario.getAyudasNecesarias());
        response.setPersonasEnHogar(beneficiario.getPersonasEnHogar());
        response.setActivo(beneficiario.getActivo());
        return response;
    }
}
