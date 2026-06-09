package com.uade.tpo.wepadel.persistencia.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "configuracion_sistema")
public class ConfiguracionSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "costo_envio", nullable = false)
    private BigDecimal costoEnvio;

    @Column(name = "canal_notificacion_default", nullable = false)
    private String canalNotificacionDefault;

    @Column(name = "pesos_por_punto_generado", nullable = false)
    private BigDecimal pesosPorPuntoGenerado;

    @Column(name = "conversion_puntos", nullable = false)
    private int conversionPuntos;
}
