package com.api.refactor.repository;

import com.api.refactor.entity.Solicitud;
import com.api.refactor.enums.EstadoSolicitud;
import com.api.refactor.enums.TipoDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByBeneficiarioId(Long beneficiarioId);
    List<Solicitud> findByEstado(EstadoSolicitud estado);
    List<Solicitud> findByBeneficiarioIdAndEstado(Long beneficiarioId, EstadoSolicitud estado);
    List<Solicitud> findByTipoAyudaAndEstado(TipoDonacion tipoAyuda, EstadoSolicitud estado);
}
