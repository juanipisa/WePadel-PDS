package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegistroDialog extends JDialog {

    private final JTextField nombreField = new JTextField(20);
    private final JTextField mailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final JButton registrarButton = new JButton("Registrar");
    private final JButton cancelarButton = new JButton("Cancelar");
    private boolean registrado;

    public RegistroDialog(Window owner) {
        super(owner, "Registro de cliente", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Nombre y apellido:"));
        form.add(nombreField);
        form.add(new JLabel("Email:"));
        form.add(mailField);
        form.add(new JLabel("Contrasena:"));
        form.add(passwordField);
        form.add(new JLabel("Confirmar:"));
        form.add(confirmField);
        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(registrarButton);
        buttons.add(cancelarButton);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public JTextField getNombreField() {
        return nombreField;
    }

    public JTextField getMailField() {
        return mailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getConfirmField() {
        return confirmField;
    }

    public JButton getRegistrarButton() {
        return registrarButton;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }
}
