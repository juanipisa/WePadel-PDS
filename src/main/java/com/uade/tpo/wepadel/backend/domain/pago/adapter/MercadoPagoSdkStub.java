package com.uade.tpo.wepadel.backend.domain.pago.adapter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Stub que simula el SDK de Mercado Pago sin dependencias externas.
 * En produccion se reemplazaria por el cliente real de la API.
 */
public class MercadoPagoSdkStub implements PasarelaPagoExterna {

    @Override
    public RespuestaPasarela cobrar(BigDecimal monto, String referenciaPago) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return new RespuestaPasarela(false, "Monto invalido", null);
        }
        if (referenciaPago == null || referenciaPago.isBlank()) {
            return new RespuestaPasarela(false, "Referencia de pago requerida", null);
        }
        String codigo = "MP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new RespuestaPasarela(true, "Pago con Mercado Pago aprobado", codigo);
    }
}
