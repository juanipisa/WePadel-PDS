package com.uade.tpo.wepadel.domain.notificacion;

import com.uade.tpo.wepadel.domain.pedido.Observador;
import com.uade.tpo.wepadel.domain.pedido.Pedido;
import com.uade.tpo.wepadel.domain.pedido.estado.EstadoPedido;
import com.uade.tpo.wepadel.domain.usuario.Cliente;

public abstract class Notificador implements Observador {

    protected Cliente destinatario;

    protected Notificador(Cliente destinatario) {
        this.destinatario = destinatario;
    }

    @Override
    public void actualizar(Pedido pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo) {
        String mensaje = String.format(
                "Pedido #%d cambio de %s a %s. Total: $%s",
                pedido.getId(),
                estadoAnterior.getNombre(),
                estadoNuevo.getNombre(),
                pedido.getTotal());
        enviar(mensaje);
    }

    public abstract void enviar(String mensaje);

    public Cliente getDestinatario() {
        return destinatario;
    }
}
