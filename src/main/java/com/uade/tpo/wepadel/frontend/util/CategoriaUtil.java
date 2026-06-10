package com.uade.tpo.wepadel.frontend.util;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.ComponenteCatalogo;

public final class CategoriaUtil {

    public record CategoriaItem(String ruta, Categoria categoria) {
        @Override
        public String toString() {
            return ruta;
        }
    }

    private CategoriaUtil() {
    }

    public static List<CategoriaItem> listarTodas(Categoria raiz) {
        List<CategoriaItem> items = new ArrayList<>();
        recorrer(raiz, raiz.getNombre(), items);
        return items;
    }

    private static void recorrer(Categoria categoria, String ruta, List<CategoriaItem> items) {
        items.add(new CategoriaItem(ruta, categoria));
        for (ComponenteCatalogo hijo : categoria.getHijos()) {
            if (hijo instanceof Categoria sub) {
                recorrer(sub, ruta + " > " + sub.getNombre(), items);
            }
        }
    }
}
