package com.uade.tpo.wepadel.backend.config;

import java.math.BigDecimal;

/** Patron SINGLETON: una sola instancia con parametros globales de la tienda. */
public final class ConfiguracionSistema {

    private static final ConfiguracionSistema INSTANCIA = new ConfiguracionSistema();

    private BigDecimal costoEnvio = new BigDecimal("1500.00");
    private String canalNotificacionDefault = "EMAIL";
    /** 1 punto generado por cada X pesos pagados (despues de descuentos). */
    private BigDecimal pesosPorPuntoGenerado = new BigDecimal("10.00");
    private int conversionPuntos = 100;

    private ConfiguracionSistema() {
    }

    public static ConfiguracionSistema getInstancia() {
        return INSTANCIA;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getCanalNotificacionDefault() {
        return canalNotificacionDefault;
    }

    public void setCanalNotificacionDefault(String canalNotificacionDefault) {
        this.canalNotificacionDefault = canalNotificacionDefault;
    }

    public BigDecimal getPesosPorPuntoGenerado() {
        return pesosPorPuntoGenerado;
    }

    public int getConversionPuntos() {
        return conversionPuntos;
    }

    public void setConversionPuntos(int conversionPuntos) {
        this.conversionPuntos = conversionPuntos;
    }

    public void setPesosPorPuntoGenerado(BigDecimal pesosPorPuntoGenerado) {
        this.pesosPorPuntoGenerado = pesosPorPuntoGenerado;
    }
}
