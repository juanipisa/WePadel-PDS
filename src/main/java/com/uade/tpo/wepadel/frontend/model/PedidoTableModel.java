package com.uade.tpo.wepadel.frontend.model;

import java.math.BigDecimal;
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
            columnas = new String[]{
                    "ID", "Cliente", "Fecha", "Total pagado", "Descuento", "Puntos usados", "Estado", "Cod. transaccion"
            };
        } else {
            columnas = new String[]{
                    "ID", "Fecha", "Total pagado", "Descuento", "Puntos usados", "Estado", "Cod. transaccion"
            };
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
                case 3 -> formatearMonto(p.getTotal());
                case 4 -> formatearDescuento(p);
                case 5 -> formatearPuntosUsados(p);
                case 6 -> p.getEstado().getNombre();
                case 7 -> p.getCodigoTransaccion() != null ? p.getCodigoTransaccion() : "-";
                default -> "";
            };
        }
        return switch (columnIndex) {
            case 0 -> p.getId();
            case 1 -> p.getFechaCompra();
            case 2 -> formatearMonto(p.getTotal());
            case 3 -> formatearDescuento(p);
            case 4 -> formatearPuntosUsados(p);
            case 5 -> p.getEstado().getNombre();
            case 6 -> p.getCodigoTransaccion() != null ? p.getCodigoTransaccion() : "-";
            default -> "";
        };
    }

    private String formatearMonto(BigDecimal monto) {
        return "$" + monto;
    }

    private String formatearDescuento(Pedido pedido) {
        BigDecimal descuento = pedido.getDescuentoPorPuntos();
        if (!pedido.isUsaPuntos() || descuento == null || descuento.compareTo(BigDecimal.ZERO) <= 0) {
            return "-";
        }
        return "$" + descuento;
    }

    private String formatearPuntosUsados(Pedido pedido) {
        if (!pedido.isUsaPuntos() || pedido.getPuntosUsados() <= 0) {
            return "-";
        }
        return String.valueOf(pedido.getPuntosUsados());
    }
}
