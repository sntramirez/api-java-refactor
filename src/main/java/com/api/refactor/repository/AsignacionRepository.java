package com.api.refactor.repository;

import com.api.refactor.entity.Asignacion;
import com.api.refactor.enums.EstadoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByBeneficiarioId(Long beneficiarioId);
    List<Asignacion> findByEstado(EstadoAsignacion estado);
    Optional<Asignacion> findByDonacionId(Long donacionId);
    List<Asignacion> findByBeneficiarioIdAndEstado(Long beneficiarioId, EstadoAsignacion estado);
    long countByEstado(EstadoAsignacion estado);
}
