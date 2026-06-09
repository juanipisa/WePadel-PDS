package com.uade.tpo.wepadel.backend.domain.notificacion;

import com.uade.tpo.wepadel.backend.domain.pedido.Observador;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.backend.domain.pedido.estado.EstadoPedido;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;

/** OBSERVER concreto: notifica al cliente por email, SMS o push. */
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
