package com.api.refactor.service;

import com.api.refactor.dto.request.DonacionRequest;
import com.api.refactor.dto.response.DonacionResponse;
import com.api.refactor.entity.Donacion;
import com.api.refactor.entity.Donante;
import com.api.refactor.enums.EstadoDonacion;
import com.api.refactor.enums.TipoDonacion;
import com.api.refactor.repository.DonacionRepository;
import com.api.refactor.repository.DonanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonacionService {

    private final DonacionRepository donacionRepository;
    private final DonanteRepository donanteRepository;

    @Transactional
    public DonacionResponse crearDonacion(DonacionRequest request) {
        Donante donante = donanteRepository.findByEmail(request.getEmailDonante())
                .orElseGet(() -> crearNuevoDonante(request));

        Donacion donacion = new Donacion();
        donacion.setTipo(request.getTipo());
        donacion.setCantidad(request.getCantidad());
        donacion.setDescripcion(request.getDescripcion());
        donacion.setDonante(donante);
        donacion.setEstado(EstadoDonacion.NUEVA);

        donacion = donacionRepository.save(donacion);

        return mapToResponse(donacion);
    }

    private Donante crearNuevoDonante(DonacionRequest request) {
        Donante donante = new Donante();
        donante.setNombre(request.getNombreDonante());
        donante.setTelefono(request.getTelefonoDonante());
        donante.setEmail(request.getEmailDonante());
        donante.setComentarios(request.getComentarios());
        return donanteRepository.save(donante);
    }

    public List<DonacionResponse> obtenerDonacionesPorEstado(EstadoDonacion estado) {
        return donacionRepository.findByEstado(estado).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DonacionResponse> obtenerDonacionesDisponibles() {
        return donacionRepository.findByTipoAndEstado(null, EstadoDonacion.REVISADA).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DonacionResponse actualizarEstado(Long id, EstadoDonacion nuevoEstado) {
        Donacion donacion = donacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donación no encontrada"));

        donacion.setEstado(nuevoEstado);
        donacion = donacionRepository.save(donacion);

        return mapToResponse(donacion);
    }

    private DonacionResponse mapToResponse(Donacion donacion) {
        DonacionResponse response = new DonacionResponse();
        response.setId(donacion.getId());
        response.setTipo(donacion.getTipo());
        response.setCantidad(donacion.getCantidad());
        response.setDescripcion(donacion.getDescripcion());
        response.setNombreDonante(donacion.getDonante().getNombre());
        response.setEstado(donacion.getEstado());
        response.setFechaDonacion(donacion.getFechaDonacion());
        response.setNumeroReferencia(donacion.getNumeroReferencia());
        return response;
    }
}
