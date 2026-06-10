package com.uade.tpo.wepadel.frontend.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.frontend.util.ProductoUtil;

public class ProductoTableModel extends AbstractTableModel {

    private final String[] columnas = {"ID", "Nombre", "Precio", "Stock", "Tipo", "Descripcion tecnica", "Habilitado"};
    private List<Producto> productos = new ArrayList<>();

    public void setProductos(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
        fireTableDataChanged();
    }

    public Producto getProductoAt(int row) {
        return productos.get(row);
    }

    public List<Producto> getProductos() {
        return List.copyOf(productos);
    }

    @Override
    public int getRowCount() {
        return productos.size();
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
        Producto p = productos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getId();
            case 1 -> p.getNombre();
            case 2 -> p.getPrecio();
            case 3 -> p.getStock();
            case 4 -> ProductoUtil.tipoProducto(p);
            case 5 -> p.getDescripcionTecnica();
            case 6 -> p.isHabilitado() ? "Si" : "No";
            default -> "";
        };
    }
}
