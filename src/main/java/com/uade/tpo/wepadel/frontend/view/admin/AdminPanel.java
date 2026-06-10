package com.uade.tpo.wepadel.frontend.view.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.uade.tpo.wepadel.frontend.model.PedidoTableModel;
import com.uade.tpo.wepadel.frontend.model.ProductoTableModel;
import com.uade.tpo.wepadel.frontend.model.UsuarioTableModel;

public class AdminPanel extends JPanel {

    private final JTabbedPane tabs = new JTabbedPane();

    private final PedidoTableModel pedidoTableModel = new PedidoTableModel(true);
    private final JTable pedidosTable = new JTable(pedidoTableModel);
    private final JComboBox<String> estadoCombo = new JComboBox<>(new String[]{"PAGADO", "ENVIADO", "ENTREGADO", "CANCELADO"});
    private final JButton cambiarEstadoButton = new JButton("Cambiar estado");
    private final JButton cancelarPedidoButton = new JButton("Cancelar pedido");
    private final JButton refrescarPedidosButton = new JButton("Refrescar");

    private final ProductoTableModel productoAdminTableModel = new ProductoTableModel();
    private final JTable productosTable = new JTable(productoAdminTableModel);
    private final JButton nuevoProductoButton = new JButton("Nuevo producto");
    private final JSpinner stockSpinner = new JSpinner();
    private final JButton actualizarStockButton = new JButton("Actualizar stock");
    private final JButton toggleHabilitadoButton = new JButton("Toggle habilitado");
    private final JButton refrescarProductosButton = new JButton("Refrescar");

    private final JTree categoriaTree = new JTree(
            new DefaultTreeModel(new DefaultMutableTreeNode("Cargando categorias...")));
    private final JButton nuevaCategoriaButton = new JButton("Nueva subcategoria");
    private final JButton refrescarCategoriasButton = new JButton("Refrescar arbol");

    private final UsuarioTableModel usuarioTableModel = new UsuarioTableModel();
    private final JTable usuariosTable = new JTable(usuarioTableModel);
    private final JButton refrescarUsuariosButton = new JButton("Refrescar");

    private final JTextField costoEnvioField = new JTextField(10);
    private final JTextField conversionPuntosField = new JTextField(10);
    private final JTextField pesosPorPuntoField = new JTextField(10);
    private final JTextField canalDefaultField = new JTextField(10);
    private final JButton guardarConfigButton = new JButton("Guardar configuracion");
    private final JButton volverButton = new JButton("Volver al catalogo");

    public AdminPanel() {
        setLayout(new BorderLayout());
        tabs.addTab("Pedidos", buildPedidosTab());
        tabs.addTab("Productos", buildProductosTab());
        tabs.addTab("Categorias", buildCategoriasTab());
        tabs.addTab("Usuarios", buildUsuariosTab());
        tabs.addTab("Configuracion", buildConfigTab());
        add(tabs, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(volverButton);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel buildPedidosTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(pedidosTable), BorderLayout.CENTER);
        JPanel actions = new JPanel();
        actions.add(estadoCombo);
        actions.add(cambiarEstadoButton);
        actions.add(cancelarPedidoButton);
        actions.add(refrescarPedidosButton);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildProductosTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(productosTable), BorderLayout.CENTER);
        JPanel actions = new JPanel();
        actions.add(nuevoProductoButton);
        actions.add(new javax.swing.JLabel("Stock:"));
        actions.add(stockSpinner);
        actions.add(actualizarStockButton);
        actions.add(toggleHabilitadoButton);
        actions.add(refrescarProductosButton);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCategoriasTab() {
        JPanel panel = new JPanel(new BorderLayout());
        categoriaTree.setRootVisible(false);
        categoriaTree.setShowsRootHandles(true);
        JScrollPane treeScroll = new JScrollPane(categoriaTree);
        treeScroll.setPreferredSize(new Dimension(320, 400));
        panel.add(treeScroll, BorderLayout.CENTER);
        JPanel actions = new JPanel();
        actions.add(nuevaCategoriaButton);
        actions.add(refrescarCategoriasButton);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildUsuariosTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(usuariosTable), BorderLayout.CENTER);
        JPanel actions = new JPanel();
        actions.add(refrescarUsuariosButton);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildConfigTab() {
        JPanel panel = new JPanel(new java.awt.GridLayout(5, 2, 5, 5));
        panel.add(new javax.swing.JLabel("Costo envio:"));
        panel.add(costoEnvioField);
        panel.add(new javax.swing.JLabel("Conversion puntos:"));
        panel.add(conversionPuntosField);
        panel.add(new javax.swing.JLabel("Pesos por punto generado:"));
        panel.add(pesosPorPuntoField);
        panel.add(new javax.swing.JLabel("Canal notificacion default:"));
        panel.add(canalDefaultField);
        panel.add(new javax.swing.JLabel(""));
        panel.add(guardarConfigButton);
        return panel;
    }

    public PedidoTableModel getPedidoTableModel() {
        return pedidoTableModel;
    }

    public JTable getPedidosTable() {
        return pedidosTable;
    }

    public JComboBox<String> getEstadoCombo() {
        return estadoCombo;
    }

    public JButton getCambiarEstadoButton() {
        return cambiarEstadoButton;
    }

    public JButton getCancelarPedidoButton() {
        return cancelarPedidoButton;
    }

    public JButton getRefrescarPedidosButton() {
        return refrescarPedidosButton;
    }

    public ProductoTableModel getProductoAdminTableModel() {
        return productoAdminTableModel;
    }

    public JTable getProductosTable() {
        return productosTable;
    }

    public JButton getNuevoProductoButton() {
        return nuevoProductoButton;
    }

    public JSpinner getStockSpinner() {
        return stockSpinner;
    }

    public JButton getActualizarStockButton() {
        return actualizarStockButton;
    }

    public JButton getToggleHabilitadoButton() {
        return toggleHabilitadoButton;
    }

    public JButton getRefrescarProductosButton() {
        return refrescarProductosButton;
    }

    public JTree getCategoriaTree() {
        return categoriaTree;
    }

    public JButton getNuevaCategoriaButton() {
        return nuevaCategoriaButton;
    }

    public JButton getRefrescarCategoriasButton() {
        return refrescarCategoriasButton;
    }

    public UsuarioTableModel getUsuarioTableModel() {
        return usuarioTableModel;
    }

    public JButton getRefrescarUsuariosButton() {
        return refrescarUsuariosButton;
    }

    public JTextField getCostoEnvioField() {
        return costoEnvioField;
    }

    public JTextField getConversionPuntosField() {
        return conversionPuntosField;
    }

    public JTextField getPesosPorPuntoField() {
        return pesosPorPuntoField;
    }

    public JTextField getCanalDefaultField() {
        return canalDefaultField;
    }

    public JButton getGuardarConfigButton() {
        return guardarConfigButton;
    }

    public JButton getVolverButton() {
        return volverButton;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }
}
