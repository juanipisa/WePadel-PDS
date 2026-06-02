package com.uade.tpo.wepadel.domain.notificacion;

import com.uade.tpo.wepadel.domain.usuario.Cliente;

public class NotificadorPush extends Notificador {

    public NotificadorPush(Cliente destinatario) {
        super(destinatario);
    }

    @Override
    public void enviar(String mensaje) {
        System.out.println("[PUSH -> " + destinatario.getNombreApellido() + "] " + mensaje);
    }
}
