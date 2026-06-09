package com.uade.tpo.wepadel.backend.domain.notificacion;

import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;

public class NotificadorSMS extends Notificador {

    public NotificadorSMS(Cliente destinatario) {
        super(destinatario);
    }

    @Override
    public void enviar(String mensaje) {
        System.out.println("[SMS -> " + destinatario.getNombreApellido() + "] " + mensaje);
    }
}
