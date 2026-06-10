package com.uade.tpo.wepadel.frontend.view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.app.Navigation;
import com.uade.tpo.wepadel.frontend.controller.AdminController;
import com.uade.tpo.wepadel.frontend.controller.CarritoController;
import com.uade.tpo.wepadel.frontend.controller.CatalogoController;
import com.uade.tpo.wepadel.frontend.controller.CheckoutController;
import com.uade.tpo.wepadel.frontend.controller.LoginController;
import com.uade.tpo.wepadel.frontend.controller.PedidosController;
import com.uade.tpo.wepadel.frontend.controller.PerfilController;
import com.uade.tpo.wepadel.frontend.view.admin.AdminPanel;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final CatalogoPanel catalogoPanel = new CatalogoPanel();
    private final LoginPanel loginPanel = new LoginPanel();
    private final AdminPanel adminPanel = new AdminPanel();

    private CatalogoController catalogoController;
    private AdminController adminController;

    public MainFrame(AppContext context) {
        super("WePadel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 720);
        setMinimumSize(new java.awt.Dimension(900, 500));
        setLocationRelativeTo(null);

        cards.add(catalogoPanel, Navigation.CATALOGO);
        cards.add(loginPanel, Navigation.LOGIN);
        cards.add(adminPanel, Navigation.ADMIN);
        add(cards);

        CheckoutController checkoutController = new CheckoutController(context);
        CarritoController carritoController = new CarritoController(context, this, checkoutController);
        PerfilController perfilController = new PerfilController(context, this);
        PedidosController pedidosController = new PedidosController(context, this);

        catalogoController = new CatalogoController(context, this, catalogoPanel);
        adminController = new AdminController(context, this, adminPanel, () -> catalogoController.cargarCatalogo());

        catalogoController.setAbrirCarrito(carritoController::abrir);
        catalogoController.setAbrirPerfil(perfilController::abrir);
        catalogoController.setAbrirMisPedidos(pedidosController::abrirMisPedidos);
        catalogoController.setAbrirAdmin(this::abrirAdmin);

        new LoginController(context, this, loginPanel, () -> catalogoController.actualizarBarraSesion());

        adminPanel.getProductosTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                adminController.onProductoSeleccionado();
            }
        });

        showCard(Navigation.CATALOGO);
        catalogoController.actualizarBarraSesion();
    }

    public void showCard(String name) {
        cardLayout.show(cards, name);
    }

    private void abrirAdmin() {
        adminController.cargarDatos();
        showCard(Navigation.ADMIN);
    }
}
