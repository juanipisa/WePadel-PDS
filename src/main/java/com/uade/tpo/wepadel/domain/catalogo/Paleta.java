package com.uade.tpo.wepadel.domain.catalogo;

import java.math.BigDecimal;

public class Paleta extends Producto {

    private int pesoGramos;
    private String balance;
    private String forma;
    private String material;

    public Paleta(Long id, String nombre, String descripcion, BigDecimal precio, int stock,
                  boolean habilitado, Categoria categoria, int pesoGramos, String balance,
                  String forma, String material) {
        super(id, nombre, descripcion, precio, stock, habilitado, categoria);
        this.pesoGramos = pesoGramos;
        this.balance = balance;
        this.forma = forma;
        this.material = material;
    }

    @Override
    public String getDescripcionTecnica() {
        return String.format("Peso: %dg | Balance: %s | Forma: %s | Material: %s",
                pesoGramos, balance, forma, material);
    }

    public int getPesoGramos() {
        return pesoGramos;
    }

    public String getBalance() {
        return balance;
    }

    public String getForma() {
        return forma;
    }

    public String getMaterial() {
        return material;
    }
}
