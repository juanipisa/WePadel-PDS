package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CategoriaFormDialog extends JDialog {

    private final JLabel padreLabel = new JLabel();
    private final JTextField nombreField = new JTextField(20);
    private final JButton crearButton = new JButton("Crear");
    private final JButton cancelarButton = new JButton("Cancelar");
    private boolean creada;

    public CategoriaFormDialog(Window owner) {
        super(owner, "Nueva categoria", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Categoria padre:"));
        form.add(padreLabel);
        form.add(new JLabel("Nombre:"));
        form.add(nombreField);
        add(form, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.add(crearButton);
        buttons.add(cancelarButton);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public JLabel getPadreLabel() {
        return padreLabel;
    }

    public JTextField getNombreField() {
        return nombreField;
    }

    public JButton getCrearButton() {
        return crearButton;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public boolean isCreada() {
        return creada;
    }

    public void setCreada(boolean creada) {
        this.creada = creada;
    }
}
