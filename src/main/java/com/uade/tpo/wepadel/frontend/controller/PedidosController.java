package com.uade.tpo.wepadel.frontend.controller;

import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.view.MainFrame;
import com.uade.tpo.wepadel.frontend.view.dialog.MisPedidosDialog;

public class PedidosController {

    private final AppContext context;
    private final MainFrame mainFrame;

    public PedidosController(AppContext context, MainFrame mainFrame) {
        this.context = context;
        this.mainFrame = mainFrame;
    }

    public void abrirMisPedidos() {
        if (!context.getSession().isCliente()) {
            Dialogs.error(mainFrame, "Solo clientes pueden ver sus pedidos");
            return;
        }
        MisPedidosDialog dialog = new MisPedidosDialog(mainFrame);
        refrescar(dialog);
        dialog.getRefrescarButton().addActionListener(e -> refrescar(dialog));
        dialog.getCancelarButton().addActionListener(e -> cancelar(dialog));
        dialog.getCerrarButton().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void refrescar(MisPedidosDialog dialog) {
        var cliente = context.getSession().getCliente();
        SwingExecutor.run(dialog, () -> context.getFacade().getPedidosCliente(cliente),
                pedidos -> dialog.getTableModel().setPedidos(pedidos));
    }

    private void cancelar(MisPedidosDialog dialog) {
        int row = dialog.getTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(dialog, "Seleccione un pedido");
            return;
        }
        Pedido pedido = dialog.getTableModel().getPedidoAt(row);
        if (!pedido.puedeCancelarse()) {
            Dialogs.error(dialog, "El pedido no puede cancelarse en su estado actual");
            return;
        }
        if (!Dialogs.confirm(dialog, "¿Cancelar pedido #" + pedido.getId() + "?")) {
            return;
        }
        SwingExecutor.run(dialog, () -> {
            context.getFacade().cancelarPedido(pedido);
            return null;
        }, r -> {
            Dialogs.info(dialog, "Pedido cancelado");
            refrescar(dialog);
        });
    }
}
