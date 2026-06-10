package com.uade.tpo.wepadel.backend.domain.pago.adapter;

import java.math.BigDecimal;

/**
 * Interfaz de una pasarela de pago externa (ej. SDK de Mercado Pago).
 * El patron ADAPTER envuelve esta API para exponer {@link com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago}.
 */
public interface PasarelaPagoExterna {

    RespuestaPasarela cobrar(BigDecimal monto, String referenciaPago);

    record RespuestaPasarela(boolean exitoso, String mensaje, String codigoTransaccion) {
    }
}
