package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.uade.tpo.wepadel.frontend.model.PedidoTableModel;

public class MisPedidosDialog extends JDialog {

    private final PedidoTableModel tableModel = new PedidoTableModel(false);
    private final JTable table = new JTable(tableModel);
    private final JButton cancelarButton = new JButton("Cancelar pedido");
    private final JButton refrescarButton = new JButton("Refrescar");
    private final JButton cerrarButton = new JButton("Cerrar");

    public MisPedidosDialog(Window owner) {
        super(owner, "Mis pedidos", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout());
        south.add(cancelarButton);
        south.add(refrescarButton);
        south.add(cerrarButton);
        add(south, BorderLayout.SOUTH);
        setSize(600, 350);
        setLocationRelativeTo(owner);
    }

    public PedidoTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public JButton getRefrescarButton() {
        return refrescarButton;
    }

    public JButton getCerrarButton() {
        return cerrarButton;
    }
}
