package com.api.refactor.service;

import com.api.refactor.dto.request.AsignacionRequest;
import com.api.refactor.dto.response.AsignacionResponse;
import com.api.refactor.dto.response.BeneficiarioResponse;
import com.api.refactor.dto.response.DonacionResponse;
import com.api.refactor.entity.Asignacion;
import com.api.refactor.entity.Beneficiario;
import com.api.refactor.entity.Donacion;
import com.api.refactor.entity.Solicitud;
import com.api.refactor.enums.EstadoAsignacion;
import com.api.refactor.enums.EstadoDonacion;
import com.api.refactor.repository.AsignacionRepository;
import com.api.refactor.repository.BeneficiarioRepository;
import com.api.refactor.repository.DonacionRepository;
import com.api.refactor.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final DonacionRepository donacionRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final SolicitudRepository solicitudRepository;

    @Transactional
    public AsignacionResponse crearAsignacion(AsignacionRequest request) {
        Donacion donacion = donacionRepository.findById(request.getDonacionId())
                .orElseThrow(() -> new RuntimeException("Donación no encontrada"));

        Beneficiario beneficiario = beneficiarioRepository.findById(request.getBeneficiarioId())
                .orElseThrow(() -> new RuntimeException("Beneficiario no encontrado"));

        if (donacion.getEstado() == EstadoDonacion.ASIGNADA) {
            throw new RuntimeException("La donación ya está asignada");
        }

        Asignacion asignacion = new Asignacion();
        asignacion.setDonacion(donacion);
        asignacion.setBeneficiario(beneficiario);
        asignacion.setComentarios(request.getComentarios());

        if (request.getSolicitudId() != null) {
            Solicitud solicitud = solicitudRepository.findById(request.getSolicitudId())
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
            asignacion.setSolicitud(solicitud);
        }

        donacion.setEstado(EstadoDonacion.ASIGNADA);
        donacionRepository.save(donacion);

        asignacion = asignacionRepository.save(asignacion);

        return mapToResponse(asignacion);
    }

    public List<AsignacionResponse> obtenerPorBeneficiario(Long beneficiarioId) {
        return asignacionRepository.findByBeneficiarioId(beneficiarioId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AsignacionResponse> obtenerPorEstado(EstadoAsignacion estado) {
        return asignacionRepository.findByEstado(estado).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AsignacionResponse actualizarEstado(Long id, EstadoAsignacion nuevoEstado, String comentarios) {
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        asignacion.setEstado(nuevoEstado);
        asignacion.setComentariosCambioEstado(comentarios);

        if (nuevoEstado == EstadoAsignacion.ENTREGADA) {
            asignacion.getDonacion().setEstado(EstadoDonacion.ENTREGADA);
            donacionRepository.save(asignacion.getDonacion());
        }

        asignacion = asignacionRepository.save(asignacion);

        return mapToResponse(asignacion);
    }

    private AsignacionResponse mapToResponse(Asignacion asignacion) {
        AsignacionResponse response = new AsignacionResponse();
        response.setId(asignacion.getId());
        response.setEstado(asignacion.getEstado());
        response.setFechaAsignacion(asignacion.getFechaAsignacion());
        response.setFechaActualizacion(asignacion.getFechaActualizacion());
        response.setComentarios(asignacion.getComentarios());

        // Mapear donación
        DonacionResponse donacionResponse = new DonacionResponse();
        donacionResponse.setId(asignacion.getDonacion().getId());
        donacionResponse.setTipo(asignacion.getDonacion().getTipo());
        donacionResponse.setCantidad(asignacion.getDonacion().getCantidad());
        response.setDonacion(donacionResponse);

        // Mapear beneficiario
        BeneficiarioResponse beneficiarioResponse = new BeneficiarioResponse();
        beneficiarioResponse.setId(asignacion.getBeneficiario().getId());
        beneficiarioResponse.setNombre(asignacion.getBeneficiario().getNombre());
        beneficiarioResponse.setCiudad(asignacion.getBeneficiario().getCiudad());
        response.setBeneficiario(beneficiarioResponse);

        return response;
    }
}
