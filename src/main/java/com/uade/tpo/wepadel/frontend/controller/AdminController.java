package com.uade.tpo.wepadel.frontend.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.uade.tpo.wepadel.backend.config.ConfiguracionSistema;
import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosAccesorio;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosCalzado;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosPaleta;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosPelota;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.backend.domain.usuario.Administrador;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.app.Navigation;
import com.uade.tpo.wepadel.frontend.util.CategoriaTreeBuilder;
import com.uade.tpo.wepadel.frontend.util.CategoriaUtil;
import com.uade.tpo.wepadel.frontend.util.CategoriaUtil.CategoriaItem;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.util.TreeUtil;
import com.uade.tpo.wepadel.frontend.view.MainFrame;
import com.uade.tpo.wepadel.frontend.view.admin.AdminPanel;
import com.uade.tpo.wepadel.frontend.view.dialog.CategoriaFormDialog;
import com.uade.tpo.wepadel.frontend.view.dialog.ProductoFormDialog;

public class AdminController {

    private final AppContext context;
    private final MainFrame mainFrame;
    private final AdminPanel panel;
    private final Runnable onCatalogoRefresh;
    private CategoriaTreeBuilder treeBuilder;
    private Categoria catalogoRaiz;

    public AdminController(AppContext context, MainFrame mainFrame, AdminPanel panel, Runnable onCatalogoRefresh) {
        this.context = context;
        this.mainFrame = mainFrame;
        this.panel = panel;
        this.onCatalogoRefresh = onCatalogoRefresh;
        bind();
        panel.getTabs().addChangeListener(e -> {
            if (panel.getTabs().getSelectedIndex() == 2) {
                refrescarCategorias();
            }
        });
    }

    public void cargarDatos() {
        if (!context.getSession().isAdmin()) {
            return;
        }
        refrescarPedidos();
        refrescarProductos();
        refrescarCategorias();
        refrescarUsuarios();
        cargarConfig();
    }

    private void bind() {
        panel.getVolverButton().addActionListener(e -> mainFrame.showCard(Navigation.CATALOGO));
        panel.getRefrescarPedidosButton().addActionListener(e -> refrescarPedidos());
        panel.getCambiarEstadoButton().addActionListener(e -> cambiarEstado());
        panel.getCancelarPedidoButton().addActionListener(e -> cancelarPedidoAdmin());
        panel.getRefrescarProductosButton().addActionListener(e -> refrescarProductos());
        panel.getNuevoProductoButton().addActionListener(e -> nuevoProducto());
        panel.getActualizarStockButton().addActionListener(e -> actualizarStock());
        panel.getToggleHabilitadoButton().addActionListener(e -> toggleHabilitado());
        panel.getRefrescarCategoriasButton().addActionListener(e -> refrescarCategorias());
        panel.getNuevaCategoriaButton().addActionListener(e -> nuevaCategoria());
        panel.getRefrescarUsuariosButton().addActionListener(e -> refrescarUsuarios());
        panel.getGuardarConfigButton().addActionListener(e -> guardarConfig());
    }

    private Administrador admin() {
        return context.getSession().getAdministrador();
    }

    private void refrescarPedidos() {
        SwingExecutor.run(panel, () -> context.getFacade().getPedidos(),
                pedidos -> panel.getPedidoTableModel().setPedidos(pedidos));
    }

    private void cambiarEstado() {
        int row = panel.getPedidosTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(panel, "Seleccione un pedido");
            return;
        }
        Pedido pedido = panel.getPedidoTableModel().getPedidoAt(row);
        String estado = (String) panel.getEstadoCombo().getSelectedItem();
        SwingExecutor.run(panel, () -> {
            context.getFacade().cambiarEstadoPedido(admin(), pedido, estado);
            return null;
        }, r -> {
            Dialogs.info(panel, "Estado actualizado");
            refrescarPedidos();
        });
    }

    private void cancelarPedidoAdmin() {
        int row = panel.getPedidosTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(panel, "Seleccione un pedido");
            return;
        }
        Pedido pedido = panel.getPedidoTableModel().getPedidoAt(row);
        if (!Dialogs.confirm(panel, "¿Cancelar pedido #" + pedido.getId() + "?")) {
            return;
        }
        SwingExecutor.run(panel, () -> {
            context.getFacade().cancelarPedido(pedido);
            return null;
        }, r -> {
            Dialogs.info(panel, "Pedido cancelado");
            refrescarPedidos();
        });
    }

    private void refrescarProductos() {
        SwingExecutor.run(panel, () -> context.getFacade().listarTodosLosProductos(),
                productos -> panel.getProductoAdminTableModel().setProductos(productos));
    }

    private void actualizarStock() {
        int row = panel.getProductosTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(panel, "Seleccione un producto");
            return;
        }
        Producto producto = panel.getProductoAdminTableModel().getProductoAt(row);
        int stock = (Integer) panel.getStockSpinner().getValue();
        SwingExecutor.run(panel, () -> {
            context.getFacade().actualizarStock(admin(), producto, stock);
            return null;
        }, r -> {
            Dialogs.info(panel, "Stock actualizado");
            refrescarProductos();
            onCatalogoRefresh.run();
        });
    }

    private void toggleHabilitado() {
        int row = panel.getProductosTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(panel, "Seleccione un producto");
            return;
        }
        Producto producto = panel.getProductoAdminTableModel().getProductoAt(row);
        boolean nuevo = !producto.isHabilitado();
        SwingExecutor.run(panel, () -> {
            context.getFacade().cambiarHabilitadoProducto(admin(), producto, nuevo);
            return null;
        }, r -> {
            Dialogs.info(panel, "Producto " + (nuevo ? "habilitado" : "deshabilitado"));
            refrescarProductos();
            onCatalogoRefresh.run();
        });
    }

    private void refrescarCategorias() {
        SwingExecutor.run(panel, () -> context.getFacade().refrescarCatalogo(), raiz -> {
            catalogoRaiz = raiz;
            treeBuilder = CategoriaTreeBuilder.build(raiz);
            panel.getCategoriaTree().setModel(treeBuilder.getModel());
            panel.getCategoriaTree().setRootVisible(!treeBuilder.isRaizVirtual());
            TreeUtil.expandirTodo(panel.getCategoriaTree());
        });
    }

    private void nuevaCategoria() {
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) panel.getCategoriaTree().getLastSelectedPathComponent();
        if (nodo == null || treeBuilder == null) {
            Dialogs.error(panel, "Seleccione categoria padre en el arbol");
            return;
        }
        Categoria padre = treeBuilder.getCategoria(nodo);
        if (padre == null) {
            Dialogs.error(panel, "Seleccione una categoria valida");
            return;
        }
        CategoriaFormDialog dialog = new CategoriaFormDialog(mainFrame);
        dialog.getPadreLabel().setText(padre.getNombre());
        dialog.getCrearButton().addActionListener(e -> {
            String nombre = dialog.getNombreField().getText().trim();
            if (nombre.isBlank()) {
                Dialogs.error(dialog, "Ingrese nombre de categoria");
                return;
            }
            Categoria padreFinal = padre;
            SwingExecutor.run(dialog, () -> context.getFacade().crearCategoria(admin(), nombre, padreFinal),
                    cat -> {
                        dialog.setCreada(true);
                        dialog.dispose();
                        Dialogs.info(panel, "Categoria creada");
                        refrescarCategorias();
                        onCatalogoRefresh.run();
                    });
        });
        dialog.getCancelarButton().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void nuevoProducto() {
        if (catalogoRaiz == null) {
            Dialogs.error(panel, "Cargue el catalogo primero");
            return;
        }
        ProductoFormDialog dialog = new ProductoFormDialog(mainFrame);
        List<CategoriaItem> items = CategoriaUtil.listarTodas(catalogoRaiz);
        dialog.getCategoriaCombo().setModel(new DefaultComboBoxModel<>(items.toArray(new CategoriaItem[0])));
        dialog.getTipoCombo().addActionListener(e -> {
            String tipo = (String) dialog.getTipoCombo().getSelectedItem();
            ((java.awt.CardLayout) dialog.getCamposEspecificos().getLayout()).show(dialog.getCamposEspecificos(), tipo);
        });
        dialog.getCrearButton().addActionListener(e -> crearProducto(dialog));
        dialog.getCancelarButton().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void crearProducto(ProductoFormDialog dialog) {
        try {
            String nombre = dialog.getNombreField().getText().trim();
            String descripcion = dialog.getDescripcionField().getText().trim();
            BigDecimal precio = new BigDecimal(dialog.getPrecioField().getText().trim());
            int stock = Integer.parseInt(dialog.getStockField().getText().trim());
            CategoriaItem item = (CategoriaItem) dialog.getCategoriaCombo().getSelectedItem();
            if (item == null || nombre.isBlank()) {
                Dialogs.error(dialog, "Complete los campos obligatorios");
                return;
            }
            Categoria categoria = item.categoria();
            String tipo = (String) dialog.getTipoCombo().getSelectedItem();
            Administrador admin = admin();

            SwingExecutor.run(dialog, () -> {
                switch (tipo) {
                    case "Paleta" -> context.getFacade().crearPaleta(admin, new DatosPaleta(
                            nombre, descripcion, precio, stock, categoria,
                            Integer.parseInt(dialog.getPesoField().getText().trim()),
                            dialog.getBalanceField().getText().trim(),
                            dialog.getFormaField().getText().trim(),
                            dialog.getMaterialPaletaField().getText().trim()));
                    case "Pelota" -> context.getFacade().crearPelota(admin, new DatosPelota(
                            nombre, descripcion, precio, stock, categoria,
                            dialog.getPresionField().getText().trim(),
                            Integer.parseInt(dialog.getUnidadesField().getText().trim())));
                    case "Accesorio" -> context.getFacade().crearAccesorio(admin, new DatosAccesorio(
                            nombre, descripcion, precio, stock, categoria,
                            dialog.getTipoAccesorioField().getText().trim(),
                            dialog.getMaterialAccField().getText().trim()));
                    case "Calzado" -> context.getFacade().crearCalzado(admin, new DatosCalzado(
                            nombre, descripcion, precio, stock, categoria,
                            Integer.parseInt(dialog.getTalleField().getText().trim()),
                            dialog.getColorField().getText().trim(),
                            dialog.getGeneroField().getText().trim()));
                    default -> throw new IllegalArgumentException("Tipo no soportado");
                }
                return null;
            }, r -> {
                dialog.setCreado(true);
                dialog.dispose();
                Dialogs.info(panel, "Producto creado");
                refrescarProductos();
                onCatalogoRefresh.run();
            });
        } catch (NumberFormatException ex) {
            Dialogs.error(dialog, "Verifique los valores numericos");
        }
    }

    private void refrescarUsuarios() {
        SwingExecutor.run(panel, () -> context.getFacade().getUsuarios(),
                usuarios -> panel.getUsuarioTableModel().setUsuarios(usuarios));
    }

    private void cargarConfig() {
        ConfiguracionSistema config = context.getFacade().getConfiguracion();
        panel.getCostoEnvioField().setText(config.getCostoEnvio().toPlainString());
        panel.getConversionPuntosField().setText(String.valueOf(config.getConversionPuntos()));
        panel.getPesosPorPuntoField().setText(config.getPesosPorPuntoGenerado().toPlainString());
        panel.getCanalDefaultField().setText(config.getCanalNotificacionDefault());
    }

    private void guardarConfig() {
        try {
            BigDecimal costoEnvio = new BigDecimal(panel.getCostoEnvioField().getText().trim());
            int conversion = Integer.parseInt(panel.getConversionPuntosField().getText().trim());
            BigDecimal pesosPorPunto = new BigDecimal(panel.getPesosPorPuntoField().getText().trim());
            String canal = panel.getCanalDefaultField().getText().trim();
            SwingExecutor.run(panel, () -> {
                context.getFacade().guardarConfiguracion(costoEnvio, conversion, pesosPorPunto, canal);
                return null;
            }, r -> Dialogs.info(panel, "Configuracion guardada"));
        } catch (NumberFormatException ex) {
            Dialogs.error(panel, "Valores de configuracion invalidos");
        }
    }

    public void onProductoSeleccionado() {
        int row = panel.getProductosTable().getSelectedRow();
        if (row >= 0) {
            Producto p = panel.getProductoAdminTableModel().getProductoAt(row);
            panel.getStockSpinner().setModel(new SpinnerNumberModel(p.getStock(), 0, 99999, 1));
        }
    }
}
