package com.uade.tpo.wepadel.backend.domain.pago;

import java.math.BigDecimal;
import java.util.UUID;

/** STRATEGY concreta: valida tarjeta y simula el cobro. */
public class PagoTarjetaCredito implements MetodoDePago {

    private String numeroTarjeta;
    private String titular;
    private String cvv;
    private String vencimiento;

    public PagoTarjetaCredito(String numeroTarjeta, String titular, String cvv, String vencimiento) {
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
        this.cvv = cvv;
        this.vencimiento = vencimiento;
    }

    @Override
    public ResultadoPago procesarPago(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return new ResultadoPago(false, "Monto invalido", null);
        }
        if (numeroTarjeta == null || numeroTarjeta.replaceAll("\\D", "").length() != 16) {
            return new ResultadoPago(false, "Numero de tarjeta invalido", null);
        }
        if (cvv == null || cvv.length() < 3) {
            return new ResultadoPago(false, "CVV invalido", null);
        }
        return new ResultadoPago(true, "Pago con tarjeta aprobado",
                "TC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}
