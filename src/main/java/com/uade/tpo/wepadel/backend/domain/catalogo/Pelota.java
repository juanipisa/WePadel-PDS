package com.uade.tpo.wepadel.backend.domain.catalogo;

import java.math.BigDecimal;

public class Pelota extends Producto {

    private String presion;
    private int unidadesPorTubo;

    public Pelota(Long id, String nombre, String descripcion, BigDecimal precio, int stock,
                  boolean habilitado, Categoria categoria, String presion, int unidadesPorTubo) {
        super(id, nombre, descripcion, precio, stock, habilitado, categoria);
        this.presion = presion;
        this.unidadesPorTubo = unidadesPorTubo;
    }

    @Override
    public String getDescripcionTecnica() {
        return String.format("Presion: %s | Unidades por tubo: %d", presion, unidadesPorTubo);
    }

    public String getPresion() {
        return presion;
    }

    public int getUnidadesPorTubo() {
        return unidadesPorTubo;
    }
}
