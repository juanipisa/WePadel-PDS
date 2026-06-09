package com.uade.tpo.wepadel.persistencia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.persistencia.entity.SistemaPuntos;
import com.uade.tpo.wepadel.persistencia.entity.Usuario;
import com.uade.tpo.wepadel.persistencia.repository.SistemaPuntosRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuntosService {

    private final SistemaPuntosRepository sistemaPuntosRepository;
    private final ConfiguracionService configuracionService;

    @Transactional
    public SistemaPuntos crearSistemaPuntos(Usuario usuario) {
        int conversion = configuracionService.getConfiguracion().getConversionPuntos();
        return sistemaPuntosRepository.save(new SistemaPuntos(usuario, conversion));
    }

    @Transactional
    public void sincronizarPuntos(Cliente cliente) {
        SistemaPuntos puntos = sistemaPuntosRepository.findByUsuarioId(cliente.getId())
                .orElseThrow(() -> new IllegalStateException("Sistema de puntos no encontrado"));
        puntos.setCantidad(cliente.getSaldoPuntos());
        sistemaPuntosRepository.save(puntos);
    }

    @Transactional
    public void cargarPuntosEnCliente(Cliente cliente) {
        sistemaPuntosRepository.findByUsuarioId(cliente.getId()).ifPresent(puntos -> {
            int diferencia = puntos.getCantidad() - cliente.getSaldoPuntos();
            if (diferencia != 0) {
                cliente.getSistemaPuntos().ajustarPuntos(diferencia);
            }
        });
    }
}
