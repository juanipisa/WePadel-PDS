package com.uade.tpo.wepadel.frontend.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.ComponenteCatalogo;

public final class CategoriaTreeBuilder {

    private final Map<DefaultMutableTreeNode, Categoria> categoriaPorNodo = new HashMap<>();

    private CategoriaTreeBuilder() {
    }

    public static CategoriaTreeBuilder build(Categoria raiz) {
        CategoriaTreeBuilder builder = new CategoriaTreeBuilder();
        boolean raizVirtual = Categoria.NOMBRE_RAIZ_VIRTUAL.equals(raiz.getNombre());
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
                raizVirtual ? "Categorias" : raiz.getNombre());
        if (!raizVirtual) {
            builder.categoriaPorNodo.put(rootNode, raiz);
        }
        builder.agregarHijos(raiz, rootNode);
        builder.model = new DefaultTreeModel(rootNode);
        builder.raizVirtual = raizVirtual;
        return builder;
    }

    private boolean raizVirtual;

    private DefaultTreeModel model;

    private void agregarHijos(Categoria categoria, DefaultMutableTreeNode nodoPadre) {
        for (ComponenteCatalogo hijo : categoria.getHijos()) {
            if (hijo instanceof Categoria sub) {
                DefaultMutableTreeNode nodoHijo = new DefaultMutableTreeNode(sub.getNombre());
                categoriaPorNodo.put(nodoHijo, sub);
                nodoPadre.add(nodoHijo);
                agregarHijos(sub, nodoHijo);
            }
        }
    }

    public DefaultTreeModel getModel() {
        return model;
    }

    public Categoria getCategoria(DefaultMutableTreeNode nodo) {
        return categoriaPorNodo.get(nodo);
    }

    public boolean isRaizVirtual() {
        return raizVirtual;
    }
}
