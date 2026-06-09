package com.uade.tpo.wepadel.backend.domain.notificacion;

import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;

/** Implementacion OBSERVER por canal email. */
public class NotificadorEmail extends Notificador {

    public NotificadorEmail(Cliente destinatario) {
        super(destinatario);
    }

    @Override
    public void enviar(String mensaje) {
        System.out.println("[EMAIL -> " + destinatario.getMail() + "] " + mensaje);
    }
}
