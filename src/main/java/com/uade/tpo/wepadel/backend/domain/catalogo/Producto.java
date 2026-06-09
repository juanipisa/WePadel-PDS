package com.uade.tpo.wepadel.backend.domain.catalogo;

import java.math.BigDecimal;
import java.util.List;

/** POO — Herencia: Paleta, Pelota, Accesorio y Calzado. Tambien participa del COMPOSITE. */
public abstract class Producto implements ComponenteCatalogo {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private boolean habilitado;
    private Categoria categoria;

    protected Producto(Long id, String nombre, String descripcion, BigDecimal precio,
                       int stock, boolean habilitado, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.habilitado = habilitado;
        this.categoria = categoria;
    }

    public void descontarStock(int cant) {
        if (!hayStockSuficiente(cant)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + nombre);
        }
        stock -= cant;
    }

    public void reponerStock(int cant) {
        if (cant <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser mayor a cero");
        }
        stock += cant;
    }

    public boolean hayStockSuficiente(int cant) {
        return habilitado && cant > 0 && stock >= cant;
    }

    public abstract String getDescripcionTecnica();

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public List<Producto> getProductos() {
        return List.of(this);
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
