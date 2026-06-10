package com.uade.tpo.wepadel.persistencia.service;

import java.math.BigDecimal;

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

    @Transactional
    public void guardarConfiguracion(BigDecimal costoEnvio, int conversionPuntos, BigDecimal pesosPorPuntoGenerado,
                                     String canalNotificacionDefault) {
        ConfiguracionSistema config = ConfiguracionSistema.getInstancia();
        config.setCostoEnvio(costoEnvio);
        config.setConversionPuntos(conversionPuntos);
        config.setPesosPorPuntoGenerado(pesosPorPuntoGenerado);
        config.setCanalNotificacionDefault(canalNotificacionDefault);

        com.uade.tpo.wepadel.persistencia.entity.ConfiguracionSistema entity =
                configuracionSistemaRepository.findFirstByOrderByIdAsc()
                        .orElseGet(com.uade.tpo.wepadel.persistencia.entity.ConfiguracionSistema::new);
        entity.setCostoEnvio(costoEnvio);
        entity.setConversionPuntos(conversionPuntos);
        entity.setPesosPorPuntoGenerado(pesosPorPuntoGenerado);
        entity.setCanalNotificacionDefault(canalNotificacionDefault);
        configuracionSistemaRepository.save(entity);
    }
}
