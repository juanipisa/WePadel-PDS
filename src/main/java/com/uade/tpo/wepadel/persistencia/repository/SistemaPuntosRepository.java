package com.uade.tpo.wepadel.persistencia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.persistencia.entity.SistemaPuntos;

@Repository
public interface SistemaPuntosRepository extends JpaRepository<SistemaPuntos, Long> {

    Optional<SistemaPuntos> findByUsuarioId(Long usuarioId);
}
