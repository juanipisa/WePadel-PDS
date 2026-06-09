package com.uade.tpo.wepadel.backend.domain.pedido;

import com.uade.tpo.wepadel.backend.domain.pedido.estado.EstadoPedido;

/** Patron OBSERVER: se notifica cuando el pedido cambia de estado. */
public interface Observador {

    void actualizar(Pedido pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo);
}
