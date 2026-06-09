package com.uade.tpo.wepadel.backend.domain.catalogo;

import java.math.BigDecimal;

public record DatosPelota(
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        Categoria categoria,
        String presion,
        int unidadesPorTubo
) {
}
