package com.uade.tpo.wepadel.backend.domain.pedido.estado;

import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;

/** Patron STATE: cada estado define que transiciones de pedido estan permitidas. */
public interface EstadoPedido {

    void confirmar(Pedido pedido);

    void marcarPagado(Pedido pedido);

    void enviar(Pedido pedido);

    void entregar(Pedido pedido);

    void cancelar(Pedido pedido);

    String getNombre();
}
