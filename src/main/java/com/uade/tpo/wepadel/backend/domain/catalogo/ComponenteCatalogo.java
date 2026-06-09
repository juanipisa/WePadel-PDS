package com.uade.tpo.wepadel.backend.domain.catalogo;

import java.util.List;

/** Patron COMPOSITE: Categoria y Producto se tratan de forma uniforme en el arbol. */
public interface ComponenteCatalogo {

    String getNombre();

    List<Producto> getProductos();
}
