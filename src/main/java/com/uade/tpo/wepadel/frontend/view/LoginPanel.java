package com.uade.tpo.wepadel.frontend.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

    private final JTextField mailField = new JTextField(25);
    private final JPasswordField passwordField = new JPasswordField(25);
    private final JButton loginButton = new JButton("Ingresar");
    private final JButton registroButton = new JButton("Registrarse");
    private final JButton invitadoButton = new JButton("Continuar como invitado");

    public LoginPanel() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        form.add(mailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Contrasena:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(registroButton);
        buttons.add(invitadoButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        add(new JLabel("Iniciar sesion en WePadel", JLabel.CENTER), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
    }

    public JTextField getMailField() {
        return mailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getRegistroButton() {
        return registroButton;
    }

    public JButton getInvitadoButton() {
        return invitadoButton;
    }
}
