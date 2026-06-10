package com.uade.tpo.wepadel.frontend.controller;

import java.awt.CardLayout;
import java.math.BigDecimal;
import java.util.function.Consumer;

import javax.swing.SpinnerNumberModel;

import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pago.PagoTarjetaCredito;
import com.uade.tpo.wepadel.backend.domain.pago.PagoTransferencia;
import com.uade.tpo.wepadel.backend.domain.pago.adapter.MercadoPagoAdapter;
import com.uade.tpo.wepadel.backend.domain.pago.adapter.MercadoPagoSdkStub;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.view.dialog.CheckoutDialog;

public class CheckoutController {

    private final AppContext context;

    public CheckoutController(AppContext context) {
        this.context = context;
    }

    public void preparar(CheckoutDialog dialog) {
        var cliente = context.getSession().getCliente();
        var config = context.getFacade().getConfiguracion();
        BigDecimal subtotal = cliente.getCarrito().calcularTotal();
        int saldoPuntos = cliente.getSaldoPuntos();

        dialog.getSubtotalLabel().setText("$" + subtotal);
        dialog.getEnvioLabel().setText("$" + config.getCostoEnvio());
        dialog.getPuntosSpinner().setModel(new SpinnerNumberModel(0, 0, saldoPuntos, 1));
        dialog.getDescuentoLabel().setText("$0");
        dialog.getTotalLabel().setText("$" + subtotal.add(config.getCostoEnvio()));

        dialog.getPuntosSpinner().addChangeListener(e -> actualizarTotales(dialog));
        dialog.getMetodoPagoCombo().addActionListener(e -> {
            String metodo = (String) dialog.getMetodoPagoCombo().getSelectedItem();
            ((CardLayout) dialog.getPagoCards().getLayout()).show(dialog.getPagoCards(), metodo);
        });
    }

    public void bind(CheckoutDialog dialog, Consumer<Pedido> onSuccess) {
        dialog.getConfirmarButton().addActionListener(e -> confirmar(dialog, onSuccess));
        dialog.getCancelarButton().addActionListener(e -> dialog.dispose());
    }

    private void actualizarTotales(CheckoutDialog dialog) {
        var cliente = context.getSession().getCliente();
        var config = context.getFacade().getConfiguracion();
        BigDecimal subtotal = cliente.getCarrito().calcularTotal();
        int puntos = (Integer) dialog.getPuntosSpinner().getValue();
        BigDecimal descuento = cliente.canjearPuntos(puntos);
        BigDecimal total = subtotal.add(config.getCostoEnvio()).subtract(descuento);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }
        dialog.getDescuentoLabel().setText("$" + descuento);
        dialog.getTotalLabel().setText("$" + total);
    }

    private void confirmar(CheckoutDialog dialog, Consumer<Pedido> onSuccess) {
        var cliente = context.getSession().getCliente();
        int puntosUsados = (Integer) dialog.getPuntosSpinner().getValue();
        MetodoDePago metodoPago = construirMetodoPago(dialog);
        if (metodoPago == null) {
            return;
        }
        SwingExecutor.run(dialog, () -> context.getFacade().confirmarCompra(cliente, metodoPago, puntosUsados),
                pedido -> {
                    dialog.setConfirmado(true);
                    dialog.dispose();
                    onSuccess.accept(pedido);
                });
    }

    private MetodoDePago construirMetodoPago(CheckoutDialog dialog) {
        String metodo = (String) dialog.getMetodoPagoCombo().getSelectedItem();
        if ("Tarjeta".equals(metodo)) {
            return new PagoTarjetaCredito(
                    dialog.getTarjetaNumero().getText().trim(),
                    dialog.getTarjetaTitular().getText().trim(),
                    new String(dialog.getTarjetaCvv().getPassword()),
                    dialog.getTarjetaVencimiento().getText().trim());
        }
        if ("Transferencia".equals(metodo)) {
            return new PagoTransferencia(
                    dialog.getTransferenciaCbu().getText().trim(),
                    dialog.getTransferenciaBanco().getText().trim());
        }
        if ("Mercado Pago".equals(metodo)) {
            String alias = dialog.getMpAlias().getText().trim();
            if (alias.isBlank()) {
                Dialogs.error(dialog, "Ingrese alias de Mercado Pago");
                return null;
            }
            return new MercadoPagoAdapter(new MercadoPagoSdkStub(), alias);
        }
        Dialogs.error(dialog, "Seleccione un metodo de pago");
        return null;
    }
}
