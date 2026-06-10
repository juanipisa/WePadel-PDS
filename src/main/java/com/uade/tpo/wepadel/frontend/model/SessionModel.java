package com.uade.tpo.wepadel.frontend.model;

import com.uade.tpo.wepadel.backend.domain.usuario.Administrador;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.backend.domain.usuario.RolEnum;
import com.uade.tpo.wepadel.backend.domain.usuario.Usuario;

public class SessionModel {

    private Usuario usuario;

    public boolean isLoggedIn() {
        return usuario != null;
    }

    public boolean isInvitado() {
        return usuario == null;
    }

    public boolean isCliente() {
        return usuario instanceof Cliente;
    }

    public boolean isAdmin() {
        return usuario instanceof Administrador;
    }

    public Cliente getCliente() {
        return (Cliente) usuario;
    }

    public Administrador getAdministrador() {
        return (Administrador) usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void clear() {
        this.usuario = null;
    }

    public RolEnum getRol() {
        return usuario != null ? usuario.getRol() : null;
    }

    public String getNombreDisplay() {
        if (usuario == null) {
            return "Invitado";
        }
        return usuario.getNombreApellido();
    }
}
