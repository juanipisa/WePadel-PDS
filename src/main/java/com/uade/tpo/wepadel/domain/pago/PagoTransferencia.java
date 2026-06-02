package com.uade.tpo.wepadel.domain.pago;

import java.math.BigDecimal;
import java.util.UUID;

public class PagoTransferencia implements MetodoDePago {

    private String cbu;
    private String banco;

    public PagoTransferencia(String cbu, String banco) {
        this.cbu = cbu;
        this.banco = banco;
    }

    @Override
    public ResultadoPago procesarPago(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return new ResultadoPago(false, "Monto invalido", null);
        }
        if (cbu == null || cbu.replaceAll("\\D", "").length() != 22) {
            return new ResultadoPago(false, "CBU invalido", null);
        }
        if (banco == null || banco.isBlank()) {
            return new ResultadoPago(false, "Banco requerido", null);
        }
        return new ResultadoPago(true, "Transferencia registrada",
                "TR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}
