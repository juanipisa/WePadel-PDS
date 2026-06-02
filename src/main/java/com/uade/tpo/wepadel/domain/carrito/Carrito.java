package com.uade.tpo.wepadel.domain.carrito;

import com.uade.tpo.wepadel.domain.catalogo.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Carrito {

    private final List<ItemCarrito> items = new ArrayList<>();

    public void agregarItem(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (!producto.isHabilitado()) {
            throw new IllegalStateException("El producto no esta habilitado: " + producto.getNombre());
        }
        if (!producto.hayStockSuficiente(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para agregar al carrito: " + producto.getNombre());
        }

        Optional<ItemCarrito> existente = items.stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            modificarCantidad(producto, existente.get().getCantidad() + cantidad);
        } else {
            items.add(new ItemCarrito(producto, cantidad));
        }
    }

    public void modificarCantidad(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            eliminarItem(producto);
            return;
        }
        if (!producto.hayStockSuficiente(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para la cantidad solicitada");
        }

        ItemCarrito item = buscarItem(producto);
        item.setCantidad(cantidad);
    }

    public void eliminarItem(Producto producto) {
        items.removeIf(item -> item.getProducto().getId().equals(producto.getId()));
    }

    public void vaciar() {
        items.clear();
    }

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ItemCarrito> getItems() {
        return List.copyOf(items);
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    private ItemCarrito buscarItem(Producto producto) {
        return items.stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El producto no esta en el carrito"));
    }
}
