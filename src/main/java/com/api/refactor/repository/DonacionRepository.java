package com.api.refactor.repository;

import com.api.refactor.entity.Donacion;
import com.api.refactor.enums.EstadoDonacion;
import com.api.refactor.enums.TipoDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    List<Donacion> findByEstado(EstadoDonacion estado);
    List<Donacion> findByTipo(TipoDonacion tipo);
    List<Donacion> findByDonanteId(Long donanteId);
    List<Donacion> findByTipoAndEstado(TipoDonacion tipo, EstadoDonacion estado);
    long countByEstado(EstadoDonacion estado);
}
