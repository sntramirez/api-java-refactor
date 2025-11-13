package com.api.refactor.repository;

import com.api.refactor.entity.Beneficiario;
import com.api.refactor.enums.TipoDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long> {
    Optional<Beneficiario> findByCedula(String cedula);
    Optional<Beneficiario> findByEmail(String email);
    boolean existsByCedula(String cedula);

    @Query("SELECT b FROM Beneficiario b JOIN b.ayudasNecesarias a WHERE a = :tipoAyuda AND b.activo = true")
    List<Beneficiario> findByTipoAyudaNecesaria(@Param("tipoAyuda") TipoDonacion tipoAyuda);

    List<Beneficiario> findByCiudadContainingIgnoreCase(String ciudad);
}
