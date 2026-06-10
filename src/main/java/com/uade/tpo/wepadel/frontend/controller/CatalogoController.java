package com.uade.tpo.wepadel.frontend.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.app.Navigation;
import com.uade.tpo.wepadel.frontend.util.CategoriaTreeBuilder;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.util.TreeUtil;
import com.uade.tpo.wepadel.frontend.view.CatalogoPanel;
import com.uade.tpo.wepadel.frontend.view.MainFrame;

public class CatalogoController {

    private final AppContext context;
    private final MainFrame mainFrame;
    private final CatalogoPanel panel;
    private CategoriaTreeBuilder treeBuilder;
    private Runnable abrirCarrito;
    private Runnable abrirPerfil;
    private Runnable abrirMisPedidos;
    private Runnable abrirAdmin;

    public CatalogoController(AppContext context, MainFrame mainFrame, CatalogoPanel panel) {
        this.context = context;
        this.mainFrame = mainFrame;
        this.panel = panel;
        bind();
        cargarCatalogo();
    }

    public void setAbrirCarrito(Runnable abrirCarrito) {
        this.abrirCarrito = abrirCarrito;
    }

    public void setAbrirPerfil(Runnable abrirPerfil) {
        this.abrirPerfil = abrirPerfil;
    }

    public void setAbrirMisPedidos(Runnable abrirMisPedidos) {
        this.abrirMisPedidos = abrirMisPedidos;
    }

    public void setAbrirAdmin(Runnable abrirAdmin) {
        this.abrirAdmin = abrirAdmin;
    }

    private void bind() {
        panel.getBuscarButton().addActionListener(e -> buscar());
        panel.getBusquedaField().addActionListener(e -> buscar());
        panel.getCategoriaTree().addTreeSelectionListener(this::onCategoriaSeleccionada);
        panel.getAgregarCarritoButton().addActionListener(e -> agregarAlCarrito());
        panel.getCarritoButton().addActionListener(e -> {
            if (abrirCarrito != null) {
                abrirCarrito.run();
            }
        });
        panel.getLoginButton().addActionListener(e -> mainFrame.showCard(Navigation.LOGIN));
        panel.getAdminButton().addActionListener(e -> {
            if (abrirAdmin != null) {
                abrirAdmin.run();
            }
        });
        panel.getPerfilButton().addActionListener(e -> mostrarMenuPerfil());
    }

    public void actualizarBarraSesion() {
        var session = context.getSession();
        panel.actualizarSesion(session.isInvitado(), session.isCliente(), session.isAdmin(),
                session.getNombreDisplay());
    }

    public void cargarCatalogo() {
        SwingExecutor.run(panel, () -> context.getFacade().refrescarCatalogo(), raiz -> {
            treeBuilder = CategoriaTreeBuilder.build(raiz);
            panel.getCategoriaTree().setModel(treeBuilder.getModel());
            panel.getCategoriaTree().setRootVisible(!treeBuilder.isRaizVirtual());
            TreeUtil.expandirTodo(panel.getCategoriaTree());
            mostrarProductos(filtrarHabilitados(raiz.getProductos()));
        });
    }

    private void buscar() {
        String filtro = panel.getBusquedaField().getText().trim();
        SwingExecutor.run(panel, () -> context.getFacade().buscarProductos(filtro), productos ->
                mostrarProductos(filtrarHabilitados(productos)));
    }

    private void onCategoriaSeleccionada(TreeSelectionEvent e) {
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) panel.getCategoriaTree().getLastSelectedPathComponent();
        if (nodo == null || treeBuilder == null) {
            return;
        }
        Categoria categoria = treeBuilder.getCategoria(nodo);
        if (categoria == null) {
            return;
        }
        SwingExecutor.run(panel, () -> context.getFacade().buscarPorCategoria(categoria),
                productos -> mostrarProductos(filtrarHabilitados(productos)));
    }

    private List<Producto> filtrarHabilitados(List<Producto> productos) {
        return productos.stream().filter(Producto::isHabilitado).collect(Collectors.toList());
    }

    private void mostrarProductos(List<Producto> productos) {
        panel.getProductoTableModel().setProductos(productos);
    }

    private void agregarAlCarrito() {
        if (!context.getSession().isCliente()) {
            Dialogs.error(panel, "Inicia sesion como cliente para agregar al carrito");
            return;
        }
        int row = panel.getProductosTable().getSelectedRow();
        if (row < 0) {
            Dialogs.error(panel, "Seleccione un producto");
            return;
        }
        Producto producto = panel.getProductoTableModel().getProductoAt(row);
        var cliente = context.getSession().getCliente();
        SwingExecutor.run(panel, () -> {
            context.getFacade().agregarAlCarrito(cliente, producto, 1);
            return null;
        }, r -> Dialogs.info(panel, "Producto agregado al carrito"));
    }

    private void mostrarMenuPerfil() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem perfil = new JMenuItem("Mi perfil");
        perfil.addActionListener(e -> {
            if (abrirPerfil != null) {
                abrirPerfil.run();
            }
        });
        menu.add(perfil);

        if (context.getSession().isCliente()) {
            JMenuItem pedidos = new JMenuItem("Mis pedidos");
            pedidos.addActionListener(e -> {
                if (abrirMisPedidos != null) {
                    abrirMisPedidos.run();
                }
            });
            menu.add(pedidos);
        }

        if (context.getSession().isAdmin()) {
            JMenuItem admin = new JMenuItem("Panel de administracion");
            admin.addActionListener(e -> {
                if (abrirAdmin != null) {
                    abrirAdmin.run();
                }
            });
            menu.add(admin);
        }

        JMenuItem logout = new JMenuItem("Cerrar sesion");
        logout.addActionListener(e -> {
            context.getSession().clear();
            actualizarBarraSesion();
            cargarCatalogo();
            Dialogs.info(panel, "Sesion cerrada");
        });
        menu.add(logout);

        menu.show(panel.getPerfilButton(), 0, panel.getPerfilButton().getHeight());
    }
}
