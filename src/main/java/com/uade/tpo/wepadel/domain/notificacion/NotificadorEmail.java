package com.uade.tpo.wepadel.domain.notificacion;

import com.uade.tpo.wepadel.domain.usuario.Cliente;

public class NotificadorEmail extends Notificador {

    public NotificadorEmail(Cliente destinatario) {
        super(destinatario);
    }

    @Override
    public void enviar(String mensaje) {
        System.out.println("[EMAIL -> " + destinatario.getMail() + "] " + mensaje);
    }
}
