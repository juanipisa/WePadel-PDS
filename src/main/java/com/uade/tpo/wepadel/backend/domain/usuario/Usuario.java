package com.uade.tpo.wepadel.backend.domain.usuario;

/** POO — Herencia: Cliente y Administrador especializan comportamiento. */
public abstract class Usuario {

    private Long id;
    private String nombreApellido;
    private String mail;
    private String password;

    protected Usuario(Long id, String nombreApellido, String mail, String password) {
        this.id = id;
        this.nombreApellido = nombreApellido;
        this.mail = mail;
        this.password = password;
    }

    public boolean validarCredenciales(String pass) {
        return password != null && password.equals(pass);
    }

    public abstract RolEnum getRol();

    public Long getId() {
        return id;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
