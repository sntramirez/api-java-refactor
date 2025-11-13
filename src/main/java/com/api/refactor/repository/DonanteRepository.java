package com.api.refactor.repository;

import com.api.refactor.entity.Donante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonanteRepository extends JpaRepository<Donante, Long> {
    Optional<Donante> findByEmail(String email);
    boolean existsByEmail(String email);
}
