package com.uade.tpo.wepadel.domain.pedido;

import com.uade.tpo.wepadel.domain.pedido.estado.EstadoPedido;

public interface Observador {

    void actualizar(Pedido pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo);
}
