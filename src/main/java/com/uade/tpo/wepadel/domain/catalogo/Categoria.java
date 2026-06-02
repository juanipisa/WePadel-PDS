package com.uade.tpo.wepadel.domain.catalogo;

import java.util.ArrayList;
import java.util.List;

public class Categoria implements ComponenteCatalogo {

    private String nombre;
    private final List<ComponenteCatalogo> hijos = new ArrayList<>();

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(ComponenteCatalogo componente) {
        if (componente == null) {
            throw new IllegalArgumentException("El componente no puede ser nulo");
        }
        if (componente == this) {
            throw new IllegalArgumentException("Una categoria no puede agregarse a si misma");
        }
        if (contieneEnArbol(componente)) {
            throw new IllegalArgumentException("No se puede crear un ciclo en el arbol de categorias");
        }
        hijos.add(componente);
    }

    public void quitar(ComponenteCatalogo componente) {
        hijos.remove(componente);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ComponenteCatalogo> getHijos() {
        return List.copyOf(hijos);
    }

    @Override
    public List<Producto> getProductos() {
        List<Producto> productos = new ArrayList<>();
        for (ComponenteCatalogo hijo : hijos) {
            productos.addAll(hijo.getProductos());
        }
        return productos;
    }

    private boolean contieneEnArbol(ComponenteCatalogo objetivo) {
        if (!(objetivo instanceof Categoria categoriaObjetivo)) {
            return false;
        }
        return contieneRecursivo(categoriaObjetivo);
    }

    private boolean contieneRecursivo(Categoria categoriaObjetivo) {
        if (this == categoriaObjetivo) {
            return true;
        }
        for (ComponenteCatalogo hijo : hijos) {
            if (hijo instanceof Categoria categoriaHija && categoriaHija.contieneRecursivo(categoriaObjetivo)) {
                return true;
            }
        }
        return false;
    }
}
