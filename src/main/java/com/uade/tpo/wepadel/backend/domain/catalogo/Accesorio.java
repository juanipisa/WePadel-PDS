package com.uade.tpo.wepadel.backend.domain.catalogo;

import java.math.BigDecimal;

public class Accesorio extends Producto {

    private String tipo;
    private String material;

    public Accesorio(Long id, String nombre, String descripcion, BigDecimal precio, int stock,
                     boolean habilitado, Categoria categoria, String tipo, String material) {
        super(id, nombre, descripcion, precio, stock, habilitado, categoria);
        this.tipo = tipo;
        this.material = material;
    }

    @Override
    public String getDescripcionTecnica() {
        return String.format("Tipo: %s | Material: %s", tipo, material);
    }

    public String getTipo() {
        return tipo;
    }

    public String getMaterial() {
        return material;
    }
}
