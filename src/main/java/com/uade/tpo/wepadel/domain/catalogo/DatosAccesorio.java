package com.uade.tpo.wepadel.domain.catalogo;

import java.math.BigDecimal;

public record DatosAccesorio(
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        Categoria categoria,
        String tipo,
        String material
) {
}
