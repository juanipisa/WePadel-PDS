package com.uade.tpo.wepadel.persistencia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.persistencia.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByParentIsNull();

    List<Categoria> findAllByParentIsNullOrderByNombreAsc();

    List<Categoria> findByParentId(Long parentId);

    Optional<Categoria> findByNombreAndParentIsNull(String nombre);
}
