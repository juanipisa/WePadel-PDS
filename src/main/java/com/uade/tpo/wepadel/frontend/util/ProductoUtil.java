package com.uade.tpo.wepadel.frontend.util;

import com.uade.tpo.wepadel.backend.domain.catalogo.Accesorio;
import com.uade.tpo.wepadel.backend.domain.catalogo.Calzado;
import com.uade.tpo.wepadel.backend.domain.catalogo.Paleta;
import com.uade.tpo.wepadel.backend.domain.catalogo.Pelota;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;

public final class ProductoUtil {

    private ProductoUtil() {
    }

    public static String tipoProducto(Producto producto) {
        if (producto instanceof Paleta) {
            return "Paleta";
        }
        if (producto instanceof Pelota) {
            return "Pelota";
        }
        if (producto instanceof Accesorio) {
            return "Accesorio";
        }
        if (producto instanceof Calzado) {
            return "Calzado";
        }
        return "Producto";
    }
}
