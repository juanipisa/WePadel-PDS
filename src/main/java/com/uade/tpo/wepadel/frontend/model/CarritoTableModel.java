package com.uade.tpo.wepadel.frontend.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uade.tpo.wepadel.backend.domain.carrito.ItemCarrito;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;

public class CarritoTableModel extends AbstractTableModel {

    private final String[] columnas = {"Producto", "Cantidad", "Subtotal"};
    private List<ItemCarrito> items = new ArrayList<>();

    public void setItems(List<ItemCarrito> items) {
        this.items = new ArrayList<>(items);
        fireTableDataChanged();
    }

    public ItemCarrito getItemAt(int row) {
        return items.get(row);
    }

    public Producto getProductoAt(int row) {
        return items.get(row).getProducto();
    }

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemCarrito item = items.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> item.getProducto().getNombre();
            case 1 -> item.getCantidad();
            case 2 -> item.getSubtotal();
            default -> "";
        };
    }
}
