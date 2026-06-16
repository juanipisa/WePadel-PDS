package com.uade.tpo.wepadel.frontend.controller;

import java.awt.CardLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.function.Consumer;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePagoFactory;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
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
        BigDecimal envio = config.getCostoEnvio();
        int saldoPuntos = cliente.getSaldoPuntos();
        int maxCanjeables = calcularMaxPuntosCanjeables(cliente, subtotal, envio);

        dialog.getSubtotalLabel().setText("$" + subtotal);
        dialog.getEnvioLabel().setText("$" + envio);
        dialog.getPuntosDisponiblesLabel().setText(formatearPuntosDisponibles(saldoPuntos, maxCanjeables));
        configurarSpinnerPuntos(dialog, maxCanjeables);
        dialog.getDescuentoLabel().setText("$0");
        dialog.getTotalLabel().setText("$" + subtotal.add(envio));

        dialog.getPuntosSpinner().addChangeListener(e -> actualizarTotales(dialog));
        var puntosEditor = (JSpinner.DefaultEditor) dialog.getPuntosSpinner().getEditor();
        puntosEditor.getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (ajustarPuntosIngresados(dialog, false)) {
                    actualizarTotales(dialog);
                }
            }
        });
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
        int puntos = puntosCanjeablesActuales(dialog, cliente, subtotal, config.getCostoEnvio());
        BigDecimal descuento = cliente.getSistemaPuntos().calcularDescuento(puntos);
        BigDecimal total = subtotal.add(config.getCostoEnvio()).subtract(descuento);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }
        dialog.getDescuentoLabel().setText("$" + descuento);
        dialog.getTotalLabel().setText("$" + total);
    }

    private void confirmar(CheckoutDialog dialog, Consumer<Pedido> onSuccess) {
        if (!ajustarPuntosIngresados(dialog, true)) {
            return;
        }
        var cliente = context.getSession().getCliente();
        var config = context.getFacade().getConfiguracion();
        BigDecimal subtotal = cliente.getCarrito().calcularTotal();
        int puntosUsados = puntosCanjeablesActuales(dialog, cliente, subtotal, config.getCostoEnvio());
        if (puntosUsados > 0 && !cliente.getSistemaPuntos().puedeCanjear(puntosUsados)) {
            Dialogs.error(dialog, "Puntos insuficientes. Disponibles: " + cliente.getSaldoPuntos());
            return;
        }
        MetodoDePago metodoPago = construirMetodoPago(dialog);
        if (metodoPago == null) {
            return;
        }
        final int puntosConfirmados = puntosUsados;
        SwingExecutor.run(dialog, () -> context.getFacade().confirmarCompra(cliente, metodoPago, puntosConfirmados),
                pedido -> {
                    dialog.setConfirmado(true);
                    dialog.dispose();
                    onSuccess.accept(pedido);
                });
    }

    private MetodoDePago construirMetodoPago(CheckoutDialog dialog) {
        String metodo = (String) dialog.getMetodoPagoCombo().getSelectedItem();
        if ("Tarjeta".equals(metodo)) {
            return MetodoDePagoFactory.crearTarjeta(
                    dialog.getTarjetaNumero().getText().trim(),
                    dialog.getTarjetaTitular().getText().trim(),
                    new String(dialog.getTarjetaCvv().getPassword()),
                    dialog.getTarjetaVencimiento().getText().trim());
        }
        if ("Transferencia".equals(metodo)) {
            return MetodoDePagoFactory.crearTransferencia(
                    dialog.getTransferenciaCbu().getText().trim(),
                    dialog.getTransferenciaBanco().getText().trim());
        }
        if ("Mercado Pago".equals(metodo)) {
            String alias = dialog.getMpAlias().getText().trim();
            if (alias.isBlank()) {
                Dialogs.error(dialog, "Ingrese alias de Mercado Pago");
                return null;
            }
            return MetodoDePagoFactory.crearMercadoPago(alias);
        }
        Dialogs.error(dialog, "Seleccione un metodo de pago");
        return null;
    }

    private void configurarSpinnerPuntos(CheckoutDialog dialog, int maxCanjeables) {
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, maxCanjeables, 1);
        dialog.getPuntosSpinner().setModel(model);
        dialog.getPuntosSpinner().setToolTipText("Maximo canjeable en esta compra: " + maxCanjeables + " pts");
    }

    /**
     * Lee el texto del spinner, valida y ajusta al rango permitido.
     *
     * @param mostrarError si true, informa cuando el valor ingresado supera el maximo
     * @return false si se rechazo la confirmacion por valor invalido
     */
    private boolean ajustarPuntosIngresados(CheckoutDialog dialog, boolean mostrarError) {
        var cliente = context.getSession().getCliente();
        var config = context.getFacade().getConfiguracion();
        BigDecimal subtotal = cliente.getCarrito().calcularTotal();
        int max = calcularMaxPuntosCanjeables(cliente, subtotal, config.getCostoEnvio());

        JSpinner spinner = dialog.getPuntosSpinner();
        int ingresado = leerPuntosDesdeEditor(spinner);

        if (ingresado < 0) {
            if (mostrarError) {
                Dialogs.error(dialog, "Ingrese una cantidad valida de puntos (0 a " + max + ")");
            }
            spinner.setValue(0);
            return !mostrarError;
        }
        if (ingresado > max) {
            if (mostrarError) {
                Dialogs.error(dialog, "No puede canjear mas de " + max + " puntos en esta compra");
            }
            spinner.setValue(max);
            return !mostrarError;
        }
        spinner.setValue(ingresado);
        return true;
    }

    private int leerPuntosDesdeEditor(JSpinner spinner) {
        try {
            spinner.commitEdit();
            return Math.max(0, ((Number) spinner.getValue()).intValue());
        } catch (ParseException e) {
            String texto = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().getText().trim();
            if (texto.isEmpty()) {
                return 0;
            }
            try {
                return Math.max(0, Integer.parseInt(texto));
            } catch (NumberFormatException ex) {
                return -1;
            }
        }
    }

    private int puntosCanjeablesActuales(CheckoutDialog dialog, Cliente cliente,
                                       BigDecimal subtotal, BigDecimal envio) {
        int puntos = leerPuntosDesdeEditor(dialog.getPuntosSpinner());
        if (puntos < 0) {
            return 0;
        }
        int max = calcularMaxPuntosCanjeables(cliente, subtotal, envio);
        return Math.min(puntos, max);
    }

    private int calcularMaxPuntosCanjeables(Cliente cliente, BigDecimal subtotal, BigDecimal envio) {
        return cliente.getSistemaPuntos().calcularMaxCanjeables(subtotal.add(envio));
    }

    private String formatearPuntosDisponibles(int saldo, int maxCanjeables) {
        if (saldo <= 0) {
            return "0 pts";
        }
        if (maxCanjeables < saldo) {
            return saldo + " pts (max. en esta compra: " + maxCanjeables + ")";
        }
        return saldo + " pts";
    }
}
