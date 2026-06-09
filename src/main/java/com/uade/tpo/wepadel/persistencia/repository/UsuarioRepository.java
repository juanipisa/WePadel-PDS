package com.uade.tpo.wepadel.persistencia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.persistencia.entity.RolEnum;
import com.uade.tpo.wepadel.persistencia.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMail(String mail);

    Optional<Usuario> findFirstByRol(RolEnum rol);
}
