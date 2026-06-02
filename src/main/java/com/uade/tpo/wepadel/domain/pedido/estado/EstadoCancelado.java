package com.uade.tpo.wepadel.domain.pedido.estado;

import com.uade.tpo.wepadel.domain.pedido.Pedido;

public class EstadoCancelado implements EstadoPedido {

    @Override
    public void confirmar(Pedido pedido) {
        throw new IllegalStateException("El pedido fue cancelado");
    }

    @Override
    public void marcarPagado(Pedido pedido) {
        throw new IllegalStateException("El pedido fue cancelado");
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new IllegalStateException("El pedido fue cancelado");
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new IllegalStateException("El pedido fue cancelado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        // Ya esta cancelado
    }

    @Override
    public String getNombre() {
        return "CANCELADO";
    }
}
