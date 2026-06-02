package com.uade.tpo.wepadel.domain.pedido.estado;

import com.uade.tpo.wepadel.domain.pedido.Pedido;

public class EstadoEnviado implements EstadoPedido {

    @Override
    public void confirmar(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue confirmado");
    }

    @Override
    public void marcarPagado(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue pagado");
    }

    @Override
    public void enviar(Pedido pedido) {
        // Ya esta enviado
    }

    @Override
    public void entregar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoEntregado());
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("No se puede cancelar un pedido ya enviado");
    }

    @Override
    public String getNombre() {
        return "ENVIADO";
    }
}
