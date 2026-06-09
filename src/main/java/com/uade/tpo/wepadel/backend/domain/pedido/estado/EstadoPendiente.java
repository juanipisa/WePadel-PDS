package com.uade.tpo.wepadel.backend.domain.pedido.estado;

import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;

/** STATE concreto: pedido recien creado, puede pasar a PAGADO. */
public class EstadoPendiente implements EstadoPedido {

    @Override
    public void confirmar(Pedido pedido) {
        // Ya esta pendiente de confirmacion/pago
    }

    @Override
    public void marcarPagado(Pedido pedido) {
        pedido.cambiarEstado(new EstadoPagado());
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new IllegalStateException("No se puede enviar un pedido pendiente de pago");
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new IllegalStateException("No se puede entregar un pedido pendiente de pago");
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }
}
