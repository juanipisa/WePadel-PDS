package com.uade.tpo.wepadel.frontend.app;

import com.uade.tpo.wepadel.backend.facade.TiendaFacade;
import com.uade.tpo.wepadel.frontend.model.SessionModel;

public class AppContext {

    private final TiendaFacade facade;
    private final SessionModel session = new SessionModel();

    public AppContext(TiendaFacade facade) {
        this.facade = facade;
    }

    public TiendaFacade getFacade() {
        return facade;
    }

    public SessionModel getSession() {
        return session;
    }
}
