package com.uade.tpo.wepadel.frontend.controller;

import java.awt.Window;

import com.uade.tpo.wepadel.backend.domain.usuario.RolEnum;
import com.uade.tpo.wepadel.backend.domain.usuario.Usuario;
import com.uade.tpo.wepadel.frontend.app.AppContext;
import com.uade.tpo.wepadel.frontend.app.Navigation;
import com.uade.tpo.wepadel.frontend.util.Dialogs;
import com.uade.tpo.wepadel.frontend.util.SwingExecutor;
import com.uade.tpo.wepadel.frontend.view.LoginPanel;
import com.uade.tpo.wepadel.frontend.view.MainFrame;
import com.uade.tpo.wepadel.frontend.view.dialog.RegistroDialog;

public class LoginController {

    private final AppContext context;
    private final MainFrame mainFrame;
    private final LoginPanel panel;
    private final Runnable onLoginSuccess;

    public LoginController(AppContext context, MainFrame mainFrame, LoginPanel panel, Runnable onLoginSuccess) {
        this.context = context;
        this.mainFrame = mainFrame;
        this.panel = panel;
        this.onLoginSuccess = onLoginSuccess;
        bind();
    }

    private void bind() {
        panel.getLoginButton().addActionListener(e -> login());
        panel.getRegistroButton().addActionListener(e -> abrirRegistro());
        panel.getInvitadoButton().addActionListener(e -> {
            context.getSession().clear();
            onLoginSuccess.run();
            mainFrame.showCard(Navigation.CATALOGO);
        });
    }

    private void login() {
        String mail = panel.getMailField().getText().trim();
        String pass = new String(panel.getPasswordField().getPassword());
        if (mail.isBlank() || pass.isBlank()) {
            Dialogs.error(panel, "Complete email y contrasena");
            return;
        }
        SwingExecutor.run(panel, () -> context.getFacade().login(mail, pass), usuario -> {
            context.getSession().setUsuario(usuario);
            onLoginSuccess.run();
            mainFrame.showCard(Navigation.CATALOGO);
            Dialogs.info(panel, "Bienvenido/a " + usuario.getNombreApellido());
        });
    }

    private void abrirRegistro() {
        RegistroDialog dialog = new RegistroDialog(mainFrame);
        dialog.getRegistrarButton().addActionListener(ev -> registrar(dialog));
        dialog.getCancelarButton().addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void registrar(RegistroDialog dialog) {
        String nombre = dialog.getNombreField().getText().trim();
        String mail = dialog.getMailField().getText().trim();
        String pass = new String(dialog.getPasswordField().getPassword());
        String confirm = new String(dialog.getConfirmField().getPassword());
        if (nombre.isBlank() || mail.isBlank() || pass.isBlank()) {
            Dialogs.error(dialog, "Complete todos los campos");
            return;
        }
        if (!pass.equals(confirm)) {
            Dialogs.error(dialog, "Las contrasenas no coinciden");
            return;
        }
        SwingExecutor.run(dialog, () -> context.getFacade().registrarUsuario(nombre, mail, pass, RolEnum.CLIENTE),
                (Usuario usuario) -> {
                    dialog.setRegistrado(true);
                    dialog.dispose();
                    context.getSession().setUsuario(usuario);
                    onLoginSuccess.run();
                    mainFrame.showCard(Navigation.CATALOGO);
                    Dialogs.info(mainFrame, "Registro exitoso. Bienvenido/a " + usuario.getNombreApellido());
                });
    }
}
