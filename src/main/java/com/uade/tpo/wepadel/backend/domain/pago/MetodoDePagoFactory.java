package com.uade.tpo.wepadel.backend.domain.pago;

import com.uade.tpo.wepadel.backend.domain.pago.adapter.MercadoPagoAdapter;
import com.uade.tpo.wepadel.backend.domain.pago.adapter.MercadoPagoSdkStub;
import com.uade.tpo.wepadel.backend.domain.pago.adapter.PasarelaPagoExterna;

/**
 * Simple Factory: crea la estrategia {@link MetodoDePago} correcta
 * sin que el cliente instancie las clases concretas directamente.
 */
public final class MetodoDePagoFactory {

    private MetodoDePagoFactory() {
    }

    public static MetodoDePago crearTarjeta(String numeroTarjeta, String titular, String cvv, String vencimiento) {
        return new PagoTarjetaCredito(numeroTarjeta, titular, cvv, vencimiento);
    }

    public static MetodoDePago crearTransferencia(String cbu, String banco) {
        return new PagoTransferencia(cbu, banco);
    }

    public static MetodoDePago crearMercadoPago(String alias) {
        return crearMercadoPago(alias, new MercadoPagoSdkStub());
    }

    public static MetodoDePago crearMercadoPago(String alias, PasarelaPagoExterna pasarela) {
        return new MercadoPagoAdapter(pasarela, alias);
    }

    /** Reconstruye un metodo de pago de referencia al cargar un pedido persistido. */
    public static MetodoDePago reconstruirStub(TipoMetodoPago tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de pago no puede ser nulo");
        }
        return switch (tipo) {
            case TARJETA -> new PagoTarjetaCredito("0000000000000000", "N/A", "000", "01/99");
            case TRANSFERENCIA -> new PagoTransferencia("0000000000000000000000", "N/A");
            case MERCADO_PAGO -> new PagoMercadoPago("mp-ref");
        };
    }

    public static TipoMetodoPago resolverTipo(MetodoDePago metodoPago) {
        if (metodoPago instanceof PagoTarjetaCredito) {
            return TipoMetodoPago.TARJETA;
        }
        if (metodoPago instanceof PagoTransferencia) {
            return TipoMetodoPago.TRANSFERENCIA;
        }
        if (metodoPago instanceof PagoMercadoPago || metodoPago instanceof MercadoPagoAdapter) {
            return TipoMetodoPago.MERCADO_PAGO;
        }
        throw new IllegalArgumentException("Metodo de pago no soportado: " + metodoPago.getClass().getSimpleName());
    }
}
