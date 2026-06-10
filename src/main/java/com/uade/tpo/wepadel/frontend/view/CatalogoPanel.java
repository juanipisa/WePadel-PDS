package com.uade.tpo.wepadel.frontend.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;

import com.uade.tpo.wepadel.frontend.model.ProductoTableModel;

public class CatalogoPanel extends JPanel {

    private final JLabel usuarioLabel = new JLabel("Invitado");
    private final JTextField busquedaField = new JTextField(20);
    private final JButton buscarButton = new JButton("Buscar");
    private final JButton carritoButton = new JButton("Carrito");
    private final JButton perfilButton = new JButton("Perfil");
    private final JButton adminButton = new JButton("Panel Admin");
    private final JButton loginButton = new JButton("Iniciar sesion");
    private final JTree categoriaTree = new JTree();
    private final ProductoTableModel productoTableModel = new ProductoTableModel();
    private final JTable productosTable = new JTable(productoTableModel);
    private final JButton agregarCarritoButton = new JButton("Agregar al carrito");

    public CatalogoPanel() {
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(new JLabel("WePadel"));
        topBar.add(busquedaField);
        topBar.add(buscarButton);
        topBar.add(carritoButton);
        topBar.add(perfilButton);
        topBar.add(adminButton);
        topBar.add(loginButton);
        topBar.add(usuarioLabel);

        JPanel center = new JPanel(new BorderLayout());
        categoriaTree.setRootVisible(false);
        categoriaTree.setShowsRootHandles(true);

        JScrollPane treeScroll = new JScrollPane(categoriaTree);
        treeScroll.setPreferredSize(new Dimension(260, 0));
        treeScroll.setMinimumSize(new Dimension(220, 0));

        JScrollPane tableScroll = new JScrollPane(productosTable);
        productosTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        productosTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        productosTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        productosTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        productosTable.getColumnModel().getColumn(3).setPreferredWidth(55);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, tableScroll);
        split.setDividerLocation(260);
        split.setResizeWeight(0.0);
        center.add(split, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(agregarCarritoButton);

        add(topBar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        adminButton.setVisible(false);
        actualizarModoInvitado();
    }

    public void actualizarSesion(boolean invitado, boolean cliente, boolean admin, String nombre) {
        usuarioLabel.setText(nombre);
        loginButton.setVisible(invitado);
        perfilButton.setVisible(!invitado);
        carritoButton.setEnabled(cliente);
        agregarCarritoButton.setEnabled(cliente);
        adminButton.setVisible(admin);
        if (invitado) {
            carritoButton.setToolTipText("Inicia sesion para comprar");
            agregarCarritoButton.setToolTipText("Inicia sesion para comprar");
        } else {
            carritoButton.setToolTipText(null);
            agregarCarritoButton.setToolTipText(null);
        }
    }

    private void actualizarModoInvitado() {
        actualizarSesion(true, false, false, "Invitado");
    }

    public JTextField getBusquedaField() {
        return busquedaField;
    }

    public JButton getBuscarButton() {
        return buscarButton;
    }

    public JButton getCarritoButton() {
        return carritoButton;
    }

    public JButton getPerfilButton() {
        return perfilButton;
    }

    public JButton getAdminButton() {
        return adminButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JTree getCategoriaTree() {
        return categoriaTree;
    }

    public JTable getProductosTable() {
        return productosTable;
    }

    public ProductoTableModel getProductoTableModel() {
        return productoTableModel;
    }

    public JButton getAgregarCarritoButton() {
        return agregarCarritoButton;
    }
}
