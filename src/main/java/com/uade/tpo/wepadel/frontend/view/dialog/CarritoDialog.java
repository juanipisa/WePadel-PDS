package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;

import com.uade.tpo.wepadel.frontend.model.CarritoTableModel;

public class CarritoDialog extends JDialog {

    private final CarritoTableModel tableModel = new CarritoTableModel();
    private final JTable table = new JTable(tableModel);
    private final JLabel totalLabel = new JLabel("Total: $0");
    private final JSpinner cantidadSpinner = new JSpinner();
    private final JButton modificarButton = new JButton("Modificar cantidad");
    private final JButton eliminarButton = new JButton("Eliminar");
    private final JButton checkoutButton = new JButton("Ir al checkout");
    private final JButton cerrarButton = new JButton("Cerrar");

    public CarritoDialog(Window owner) {
        super(owner, "Carrito", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(10, 10));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 14f));
        checkoutButton.setFont(checkoutButton.getFont().deriveFont(Font.BOLD));

        JPanel checkoutRow = new JPanel(new BorderLayout(15, 0));
        checkoutRow.add(totalLabel, BorderLayout.WEST);
        checkoutRow.add(checkoutButton, BorderLayout.EAST);

        JPanel itemActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        itemActions.add(new JLabel("Cantidad:"));
        itemActions.add(cantidadSpinner);
        itemActions.add(modificarButton);
        itemActions.add(eliminarButton);
        itemActions.add(cerrarButton);

        footer.add(checkoutRow, BorderLayout.NORTH);
        footer.add(itemActions, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);

        setMinimumSize(new java.awt.Dimension(620, 380));
        setSize(620, 380);
        setLocationRelativeTo(owner);
    }

    public CarritoTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JSpinner getCantidadSpinner() {
        return cantidadSpinner;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getEliminarButton() {
        return eliminarButton;
    }

    public JButton getCheckoutButton() {
        return checkoutButton;
    }

    public JButton getCerrarButton() {
        return cerrarButton;
    }

    public void actualizarTotal() {
        totalLabel.setText("Total: $" + tableModel.calcularTotal());
    }
}
