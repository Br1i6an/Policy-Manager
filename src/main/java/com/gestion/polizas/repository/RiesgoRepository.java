package com.gestion.polizas.repository;

import com.gestion.polizas.model.EstadoRiesgo;
import com.gestion.polizas.model.Riesgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {

    Optional<Riesgo> findById(Long id);

    List<Riesgo> findByPolizaId(Long polizaId);

    List<Riesgo> findByPolizaIdAndEstado(Long polizaId, EstadoRiesgo estado);

    long countByPolizaId(Long polizaId);
}