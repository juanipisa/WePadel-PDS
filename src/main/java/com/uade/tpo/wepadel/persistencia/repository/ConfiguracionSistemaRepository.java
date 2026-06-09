package com.uade.tpo.wepadel.persistencia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.persistencia.entity.ConfiguracionSistema;

@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {

    Optional<ConfiguracionSistema> findFirstByOrderByIdAsc();
}
