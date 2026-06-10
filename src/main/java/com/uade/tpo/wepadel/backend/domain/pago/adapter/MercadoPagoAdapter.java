package com.uade.tpo.wepadel.backend.domain.pago.adapter;

import java.math.BigDecimal;

import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pago.ResultadoPago;

/**
 * Patron ADAPTER: adapta {@link PasarelaPagoExterna} al contrato Strategy {@link MetodoDePago}.
 */
public class MercadoPagoAdapter implements MetodoDePago {

    private final PasarelaPagoExterna pasarela;
    private final String alias;

    public MercadoPagoAdapter(PasarelaPagoExterna pasarela, String alias) {
        this.pasarela = pasarela;
        this.alias = alias;
    }

    @Override
    public ResultadoPago procesarPago(BigDecimal monto) {
        PasarelaPagoExterna.RespuestaPasarela respuesta = pasarela.cobrar(monto, alias);
        return new ResultadoPago(respuesta.exitoso(), respuesta.mensaje(), respuesta.codigoTransaccion());
    }
}
