package com.uade.tpo.wepadel.frontend.controller;

import com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorEmail;
import com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorPush;
import com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorSMS;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.view.MainFrame;
import com.uade.tpo.wepadel.frontend.view.dialog.PerfilDialog;

public class PerfilController {

    private final AppContext context;
    private final MainFrame mainFrame;

    public PerfilController(AppContext context, MainFrame mainFrame) {
        this.context = context;
        this.mainFrame = mainFrame;
    }

    public void abrir() {
        if (context.getSession().isInvitado()) {
            Dialogs.error(mainFrame, "Inicia sesion para ver tu perfil");
            return;
        }
        PerfilDialog dialog = new PerfilDialog(mainFrame);
        var usuario = context.getSession().getUsuario();
        dialog.getNombreField().setText(usuario.getNombreApellido());
        dialog.getMailField().setText(usuario.getMail());

        boolean esCliente = context.getSession().isCliente();
        dialog.setNotificacionVisible(esCliente);
        if (esCliente) {
            Cliente cliente = context.getSession().getCliente();
            dialog.getPuntosLabel().setText(String.valueOf(cliente.getSaldoPuntos()));
            dialog.getGuardarNotifButton().addActionListener(e -> guardarNotificacion(dialog, cliente));
        }

        dialog.getCerrarButton().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void guardarNotificacion(PerfilDialog dialog, Cliente cliente) {
        String canal = (String) dialog.getNotificacionCombo().getSelectedItem();
        SwingExecutor.run(dialog, () -> {
            var notificador = switch (canal) {
                case "SMS" -> new NotificadorSMS(cliente);
                case "PUSH" -> new NotificadorPush(cliente);
                default -> new NotificadorEmail(cliente);
            };
            context.getFacade().configurarNotificacion(cliente, notificador);
            return null;
        }, r -> Dialogs.info(dialog, "Preferencia de notificacion guardada"));
    }
}
