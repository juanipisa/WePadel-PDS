package com.uade.tpo.wepadel.domain.pago;

import java.math.BigDecimal;

public interface MetodoDePago {

    ResultadoPago procesarPago(BigDecimal monto);
}
