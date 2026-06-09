package com.uade.tpo.wepadel.persistencia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.config.ConfiguracionSistema;
import com.uade.tpo.wepadel.persistencia.repository.ConfiguracionSistemaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    private final ConfiguracionSistemaRepository configuracionSistemaRepository;

    @Transactional(readOnly = true)
    public void sincronizarDominio() {
        configuracionSistemaRepository.findFirstByOrderByIdAsc().ifPresent(entity -> {
            ConfiguracionSistema config = ConfiguracionSistema.getInstancia();
            config.setCostoEnvio(entity.getCostoEnvio());
            config.setCanalNotificacionDefault(entity.getCanalNotificacionDefault());
            config.setPesosPorPuntoGenerado(entity.getPesosPorPuntoGenerado());
            config.setConversionPuntos(entity.getConversionPuntos());
        });
    }

    public ConfiguracionSistema getConfiguracion() {
        sincronizarDominio();
        return ConfiguracionSistema.getInstancia();
    }
}
