package com.uade.tpo.wepadel.persistencia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.domain.notificacion.Notificador;
import com.uade.tpo.wepadel.backend.domain.usuario.Administrador;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.backend.domain.usuario.RolEnum;
import com.uade.tpo.wepadel.backend.domain.usuario.Usuario;
import com.uade.tpo.wepadel.persistencia.entity.CanalNotificacionEnum;
import com.uade.tpo.wepadel.persistencia.mapper.DomainMapper;
import com.uade.tpo.wepadel.persistencia.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DomainMapper domainMapper;
    private final CarritoService carritoService;
    private final PuntosService puntosService;
    private final IdGeneratorService idGeneratorService;

    @Transactional
    public Usuario registrarUsuario(String nombreApellido, String mail, String password, RolEnum rol) {
        if (usuarioRepository.findByMail(mail).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese mail");
        }

        com.uade.tpo.wepadel.persistencia.entity.Usuario usuarioEntity = new com.uade.tpo.wepadel.persistencia.entity.Usuario();
        usuarioEntity.setNombreApellido(nombreApellido);
        usuarioEntity.setMail(mail);
        usuarioEntity.setPassword(password);
        usuarioEntity.setRol(toEntityRol(rol));
        usuarioEntity = usuarioRepository.save(usuarioEntity);

        if (rol == RolEnum.CLIENTE) {
            carritoService.crearCarrito(usuarioEntity);
            puntosService.crearSistemaPuntos(usuarioEntity);
        }

        return construirUsuarioCompleto(usuarioEntity);
    }

    @Transactional(readOnly = true)
    public Usuario login(String mail, String pass) {
        com.uade.tpo.wepadel.persistencia.entity.Usuario usuarioEntity = usuarioRepository.findByMail(mail)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!usuarioEntity.getPassword().equals(pass)) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        return construirUsuarioCompleto(usuarioEntity);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::construirUsuarioCompleto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Administrador obtenerAdministrador() {
        com.uade.tpo.wepadel.persistencia.entity.Usuario adminEntity = usuarioRepository.findFirstByRol(
                        com.uade.tpo.wepadel.persistencia.entity.RolEnum.ADMINISTRADOR)
                .orElseThrow(() -> new IllegalStateException("No hay administrador registrado"));
        return (Administrador) construirUsuarioCompleto(adminEntity);
    }

    @Transactional
    public void configurarNotificacion(Cliente cliente, Notificador notificador) {
        cliente.setPreferenciaNotificacion(notificador);
        com.uade.tpo.wepadel.persistencia.entity.Usuario usuarioEntity = usuarioRepository.findById(cliente.getId())
                .orElseThrow();
        if (notificador instanceof com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorEmail) {
            usuarioEntity.setCanalNotificacion(CanalNotificacionEnum.EMAIL);
        } else if (notificador instanceof com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorSMS) {
            usuarioEntity.setCanalNotificacion(CanalNotificacionEnum.SMS);
        } else if (notificador instanceof com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorPush) {
            usuarioEntity.setCanalNotificacion(CanalNotificacionEnum.PUSH);
        }
        usuarioRepository.save(usuarioEntity);
    }

    private Usuario construirUsuarioCompleto(com.uade.tpo.wepadel.persistencia.entity.Usuario usuarioEntity) {
        Usuario usuario = domainMapper.toDomainUsuarioBasico(usuarioEntity);

        if (usuario instanceof Cliente cliente) {
            cliente.setGeneradorIdPedido(idGeneratorService::siguienteIdPedido);
            puntosService.cargarPuntosEnCliente(cliente);
            carritoService.cargarCarritoEnCliente(cliente);
            if (usuarioEntity.getCanalNotificacion() != null) {
                cliente.setPreferenciaNotificacion(
                        domainMapper.toNotificador(usuarioEntity.getCanalNotificacion(), cliente));
            }
        } else if (usuario instanceof Administrador admin) {
            admin.setGeneradorIdProducto(idGeneratorService::siguienteIdProducto);
        }

        return usuario;
    }

    private com.uade.tpo.wepadel.persistencia.entity.RolEnum toEntityRol(RolEnum rol) {
        return com.uade.tpo.wepadel.persistencia.entity.RolEnum.valueOf(rol.name());
    }
}
