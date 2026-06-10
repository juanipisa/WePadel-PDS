package com.uade.tpo.wepadel.frontend.controller;

import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.view.MainFrame;
import com.uade.tpo.wepadel.frontend.view.dialog.CarritoDialog;
import com.uade.tpo.wepadel.frontend.view.dialog.CheckoutDialog;

public class CarritoController {

    private final AppContext context;
    private final MainFrame mainFrame;
    private final CheckoutController checkoutController;

    public CarritoController(AppContext context, MainFrame mainFrame, CheckoutController checkoutController) {
        this.context = context;
        this.mainFrame = mainFrame;
        this.checkoutController = checkoutController;
    }

    public void abrir() {
        if (!context.getSession().isCliente()) {
            Dialogs.error(mainFrame, "Inicia sesion como cliente");
            return;
        }
        CarritoDialog dialog = new CarritoDialog(mainFrame);
        refrescar(dialog);
        dialog.getModificarButton().addActionListener(e -> modificarCantidad(dialog));
        dialog.getEliminarButton().addActionListener(e -> eliminar(dialog));
        dialog.getCheckoutButton().addActionListener(e -> abrirCheckout(dialog));
        dialog.getCerrarButton().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void refrescar(CarritoDialog dialog) {
        var cliente = context.getSession().getCliente();
        dialog.getTableModel().setItems(cliente.getCarrito().getItems());
        dialog.actualizarTotal();
    }

    private void modificarCantidad(CarritoDialog dialog) {
        int row = dialog.getTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(dialog, "Seleccione un item");
            return;
        }
        int cantidad = (Integer) dialog.getCantidadSpinner().getValue();
        Producto producto = dialog.getTableModel().getProductoAt(row);
        var cliente = context.getSession().getCliente();
        SwingExecutor.run(dialog, () -> {
            context.getFacade().modificarCantidadCarrito(cliente, producto, cantidad);
            return null;
        }, r -> refrescar(dialog));
    }

    private void eliminar(CarritoDialog dialog) {
        int row = dialog.getTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(dialog, "Seleccione un item");
            return;
        }
        Producto producto = dialog.getTableModel().getProductoAt(row);
        var cliente = context.getSession().getCliente();
        SwingExecutor.run(dialog, () -> {
            context.getFacade().eliminarDelCarrito(cliente, producto);
            return null;
        }, r -> refrescar(dialog));
    }

    private void abrirCheckout(CarritoDialog carritoDialog) {
        var cliente = context.getSession().getCliente();
        if (cliente.getCarrito().estaVacio()) {
            Dialogs.error(carritoDialog, "El carrito esta vacio");
            return;
        }
        CheckoutDialog checkoutDialog = new CheckoutDialog(mainFrame);
        checkoutController.preparar(checkoutDialog);
        checkoutController.bind(checkoutDialog, pedido -> {
            carritoDialog.dispose();
            Dialogs.info(mainFrame, "Compra confirmada. Pedido #" + pedido.getId()
                    + " - Cod: " + pedido.getCodigoTransaccion());
        });
        checkoutDialog.setVisible(true);
    }
}
