package com.uade.tpo.wepadel.domain.pedido.estado;

import com.uade.tpo.wepadel.domain.pedido.Pedido;

public class EstadoPagado implements EstadoPedido {

    @Override
    public void confirmar(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue confirmado y pagado");
    }

    @Override
    public void marcarPagado(Pedido pedido) {
        // Ya esta pagado
    }

    @Override
    public void enviar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoEnviado());
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new IllegalStateException("El pedido debe enviarse antes de entregarse");
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public String getNombre() {
        return "PAGADO";
    }
}
