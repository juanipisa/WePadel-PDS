package com.uade.tpo.wepadel.frontend.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;

public class PedidoTableModel extends AbstractTableModel {

    private final String[] columnas;
    private final boolean incluirCliente;
    private List<Pedido> pedidos = new ArrayList<>();

    public PedidoTableModel(boolean incluirCliente) {
        this.incluirCliente = incluirCliente;
        if (incluirCliente) {
            columnas = new String[]{"ID", "Cliente", "Fecha", "Total", "Estado", "Cod. transaccion"};
        } else {
            columnas = new String[]{"ID", "Fecha", "Total", "Estado", "Cod. transaccion"};
        }
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = new ArrayList<>(pedidos);
        fireTableDataChanged();
    }

    public Pedido getPedidoAt(int row) {
        return pedidos.get(row);
    }

    @Override
    public int getRowCount() {
        return pedidos.size();
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
        Pedido p = pedidos.get(rowIndex);
        if (incluirCliente) {
            return switch (columnIndex) {
                case 0 -> p.getId();
                case 1 -> p.getCliente().getNombreApellido();
                case 2 -> p.getFechaCompra();
                case 3 -> p.getTotal();
                case 4 -> p.getEstado().getNombre();
                case 5 -> p.getCodigoTransaccion();
                default -> "";
            };
        }
        return switch (columnIndex) {
            case 0 -> p.getId();
            case 1 -> p.getFechaCompra();
            case 2 -> p.getTotal();
            case 3 -> p.getEstado().getNombre();
            case 4 -> p.getCodigoTransaccion();
            default -> "";
        };
    }
}
