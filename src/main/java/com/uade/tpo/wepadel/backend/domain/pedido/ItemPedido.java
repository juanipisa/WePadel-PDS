package com.uade.tpo.wepadel.backend.domain.pedido;

import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;

import java.math.BigDecimal;

public class ItemPedido {

    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnitarioHistorico;
    private String descripcionProducto;

    public ItemPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioHistorico = producto.getPrecio();
        this.descripcionProducto = producto.getNombre() + " - " + producto.getDescripcionTecnica();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public BigDecimal getSubtotal() {
        return precioUnitarioHistorico.multiply(BigDecimal.valueOf(cantidad));
    }
}
