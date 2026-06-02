package com.uade.tpo.wepadel.domain.catalogo;

import java.math.BigDecimal;

public record DatosPaleta(
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        Categoria categoria,
        int pesoGramos,
        String balance,
        String forma,
        String material
) {
}
