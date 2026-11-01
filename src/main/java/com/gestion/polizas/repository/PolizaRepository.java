package com.gestion.polizas.repository;

import com.gestion.polizas.model.EstadoPoliza;
import com.gestion.polizas.model.Poliza;
import com.gestion.polizas.model.TipoPoliza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long>, JpaSpecificationExecutor<Poliza> {

    Optional<Poliza> findById(Long id);

    List<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado);

    List<Poliza> findByEstado(EstadoPoliza estado);

    List<Poliza> findByTipo(TipoPoliza tipo);
}