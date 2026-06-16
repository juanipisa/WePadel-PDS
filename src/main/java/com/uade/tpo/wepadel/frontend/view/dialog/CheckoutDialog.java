package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class CheckoutDialog extends JDialog {

    private final JLabel subtotalLabel = new JLabel();
    private final JLabel envioLabel = new JLabel();
    private final JLabel puntosDisponiblesLabel = new JLabel();
    private final JLabel descuentoLabel = new JLabel();
    private final JLabel totalLabel = new JLabel();
    private final JSpinner puntosSpinner = new JSpinner();
    private final JComboBox<String> metodoPagoCombo = new JComboBox<>(new String[]{"Tarjeta", "Transferencia", "Mercado Pago"});

    private final JPanel pagoCards = new JPanel(new CardLayout());
    private final JTextField tarjetaNumero = new JTextField(16);
    private final JTextField tarjetaTitular = new JTextField(20);
    private final JPasswordField tarjetaCvv = new JPasswordField(4);
    private final JTextField tarjetaVencimiento = new JTextField(5);
    private final JTextField transferenciaCbu = new JTextField(22);
    private final JTextField transferenciaBanco = new JTextField(20);
    private final JTextField mpAlias = new JTextField(20);

    private final JButton confirmarButton = new JButton("Confirmar pago");
    private final JButton cancelarButton = new JButton("Cancelar");
    private boolean confirmado;

    public CheckoutDialog(Window owner) {
        super(owner, "Checkout - Pago y confirmacion", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        JPanel resumen = new JPanel(new GridLayout(6, 2, 5, 5));
        resumen.add(new JLabel("Subtotal:"));
        resumen.add(subtotalLabel);
        resumen.add(new JLabel("Envio:"));
        resumen.add(envioLabel);
        resumen.add(new JLabel("Puntos disponibles:"));
        resumen.add(puntosDisponiblesLabel);
        resumen.add(new JLabel("Puntos a canjear:"));
        resumen.add(puntosSpinner);
        resumen.add(new JLabel("Descuento puntos:"));
        resumen.add(descuentoLabel);
        resumen.add(new JLabel("Total a pagar:"));
        resumen.add(totalLabel);

        JPanel tarjetaPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        tarjetaPanel.add(new JLabel("Numero (16 digitos):"));
        tarjetaPanel.add(tarjetaNumero);
        tarjetaPanel.add(new JLabel("Titular:"));
        tarjetaPanel.add(tarjetaTitular);
        tarjetaPanel.add(new JLabel("CVV:"));
        tarjetaPanel.add(tarjetaCvv);
        tarjetaPanel.add(new JLabel("Vencimiento (MM/AA):"));
        tarjetaPanel.add(tarjetaVencimiento);

        JPanel transferenciaPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        transferenciaPanel.add(new JLabel("CBU (22 digitos):"));
        transferenciaPanel.add(transferenciaCbu);
        transferenciaPanel.add(new JLabel("Banco:"));
        transferenciaPanel.add(transferenciaBanco);

        JPanel mpPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        mpPanel.add(new JLabel("Alias Mercado Pago:"));
        mpPanel.add(mpAlias);

        pagoCards.add(tarjetaPanel, "Tarjeta");
        pagoCards.add(transferenciaPanel, "Transferencia");
        pagoCards.add(mpPanel, "Mercado Pago");
        ((CardLayout) pagoCards.getLayout()).show(pagoCards, "Tarjeta");

        JPanel center = new JPanel(new BorderLayout());
        center.add(resumen, BorderLayout.NORTH);
        JPanel pagoSection = new JPanel(new BorderLayout());
        pagoSection.add(new JLabel("Metodo de pago:"), BorderLayout.NORTH);
        JPanel pagoTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pagoTop.add(metodoPagoCombo);
        pagoSection.add(pagoTop, BorderLayout.NORTH);
        pagoSection.add(pagoCards, BorderLayout.CENTER);
        center.add(pagoSection, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(confirmarButton);
        buttons.add(cancelarButton);
        add(buttons, BorderLayout.SOUTH);

        setMinimumSize(new java.awt.Dimension(480, 420));
        pack();
        setLocationRelativeTo(owner);
    }

    public JLabel getSubtotalLabel() {
        return subtotalLabel;
    }

    public JLabel getEnvioLabel() {
        return envioLabel;
    }

    public JLabel getPuntosDisponiblesLabel() {
        return puntosDisponiblesLabel;
    }

    public JLabel getDescuentoLabel() {
        return descuentoLabel;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JSpinner getPuntosSpinner() {
        return puntosSpinner;
    }

    public JComboBox<String> getMetodoPagoCombo() {
        return metodoPagoCombo;
    }

    public JPanel getPagoCards() {
        return pagoCards;
    }

    public JTextField getTarjetaNumero() {
        return tarjetaNumero;
    }

    public JTextField getTarjetaTitular() {
        return tarjetaTitular;
    }

    public JPasswordField getTarjetaCvv() {
        return tarjetaCvv;
    }

    public JTextField getTarjetaVencimiento() {
        return tarjetaVencimiento;
    }

    public JTextField getTransferenciaCbu() {
        return transferenciaCbu;
    }

    public JTextField getTransferenciaBanco() {
        return transferenciaBanco;
    }

    public JTextField getMpAlias() {
        return mpAlias;
    }

    public JButton getConfirmarButton() {
        return confirmarButton;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
}
