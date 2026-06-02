package com.uade.tpo.wepadel.domain.pedido.estado;

import com.uade.tpo.wepadel.domain.pedido.Pedido;

public class EstadoEntregado implements EstadoPedido {

    @Override
    public void confirmar(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue entregado");
    }

    @Override
    public void marcarPagado(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue entregado");
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue entregado");
    }

    @Override
    public void entregar(Pedido pedido) {
        // Ya esta entregado
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("No se puede cancelar un pedido entregado");
    }

    @Override
    public String getNombre() {
        return "ENTREGADO";
    }
}
