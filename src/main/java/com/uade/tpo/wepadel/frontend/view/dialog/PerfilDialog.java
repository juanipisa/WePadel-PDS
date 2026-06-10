package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PerfilDialog extends JDialog {

    private final JTextField nombreField = new JTextField(25);
    private final JTextField mailField = new JTextField(25);
    private final JLabel puntosLabel = new JLabel("0");
    private final JComboBox<String> notificacionCombo = new JComboBox<>(new String[]{"EMAIL", "SMS", "PUSH"});
    private final JButton guardarNotifButton = new JButton("Guardar notificacion");
    private final JButton cerrarButton = new JButton("Cerrar");

    public PerfilDialog(Window owner) {
        super(owner, "Mi perfil", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        nombreField.setEditable(false);
        form.add(nombreField);
        form.add(new JLabel("Email:"));
        mailField.setEditable(false);
        form.add(mailField);
        form.add(new JLabel("Puntos:"));
        form.add(puntosLabel);
        form.add(new JLabel("Canal notificacion:"));
        form.add(notificacionCombo);
        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(guardarNotifButton);
        buttons.add(cerrarButton);
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

    public JLabel getPuntosLabel() {
        return puntosLabel;
    }

    public JComboBox<String> getNotificacionCombo() {
        return notificacionCombo;
    }

    public JButton getGuardarNotifButton() {
        return guardarNotifButton;
    }

    public JButton getCerrarButton() {
        return cerrarButton;
    }

    public void setNotificacionVisible(boolean visible) {
        notificacionCombo.setVisible(visible);
        guardarNotifButton.setVisible(visible);
        puntosLabel.setVisible(visible);
        if (!visible) {
            puntosLabel.setText("-");
        }
    }
}
