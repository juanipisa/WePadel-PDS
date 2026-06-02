package com.uade.tpo.wepadel.domain.pago;

import java.math.BigDecimal;
import java.util.UUID;

public class PagoMercadoPago implements MetodoDePago {

    private String alias;

    public PagoMercadoPago(String alias) {
        this.alias = alias;
    }

    @Override
    public ResultadoPago procesarPago(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return new ResultadoPago(false, "Monto invalido", null);
        }
        if (alias == null || alias.isBlank()) {
            return new ResultadoPago(false, "Alias de Mercado Pago requerido", null);
        }
        return new ResultadoPago(true, "Pago con Mercado Pago aprobado",
                "MP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}
