package com.uade.tpo.wepadel.frontend.app;

import com.uade.tpo.wepadel.backend.facade.TiendaFacade;
import com.uade.tpo.wepadel.frontend.view.MainFrame;

public class WePadelSwingLauncher {

    private final TiendaFacade facade;

    public WePadelSwingLauncher(TiendaFacade facade) {
        this.facade = facade;
    }

    public void start() {
        AppContext context = new AppContext(facade);
        MainFrame frame = new MainFrame(context);
        frame.setVisible(true);
    }
}
