package com.uade.tpo.wepadel.backend.domain.pago;

import java.math.BigDecimal;

/** Patron STRATEGY: cada implementacion procesa el pago de forma distinta. */
public interface MetodoDePago {

    ResultadoPago procesarPago(BigDecimal monto);
}
