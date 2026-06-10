package com.uade.tpo.wepadel;

import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.uade.tpo.wepadel.backend.facade.TiendaFacade;
import com.uade.tpo.wepadel.frontend.app.WePadelSwingLauncher;

@SpringBootApplication
public class WePadelApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext ctx = SpringApplication.run(WePadelApplication.class, args);
        TiendaFacade facade = ctx.getBean(TiendaFacade.class);
        SwingUtilities.invokeLater(() -> {
            WePadelSwingLauncher launcher = new WePadelSwingLauncher(facade);
            launcher.start();
        });
    }
}
