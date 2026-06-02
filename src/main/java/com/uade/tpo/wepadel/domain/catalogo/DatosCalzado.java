package com.uade.tpo.wepadel.domain.catalogo;

import java.math.BigDecimal;

public record DatosCalzado(
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        Categoria categoria,
        int talle,
        String color,
        String genero
) {
}
