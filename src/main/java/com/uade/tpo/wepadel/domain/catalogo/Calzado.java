package com.uade.tpo.wepadel.domain.catalogo;

import java.math.BigDecimal;

public class Calzado extends Producto {

    private int talle;
    private String color;
    private String genero;

    public Calzado(Long id, String nombre, String descripcion, BigDecimal precio, int stock,
                   boolean habilitado, Categoria categoria, int talle, String color, String genero) {
        super(id, nombre, descripcion, precio, stock, habilitado, categoria);
        this.talle = talle;
        this.color = color;
        this.genero = genero;
    }

    @Override
    public String getDescripcionTecnica() {
        return String.format("Talle: %d | Color: %s | Genero: %s", talle, color, genero);
    }

    public int getTalle() {
        return talle;
    }

    public String getColor() {
        return color;
    }

    public String getGenero() {
        return genero;
    }
}
