package com.uade.tpo.wepadel.frontend.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uade.tpo.wepadel.backend.domain.usuario.Usuario;

public class UsuarioTableModel extends AbstractTableModel {

    private final String[] columnas = {"ID", "Nombre", "Mail", "Rol"};
    private List<Usuario> usuarios = new ArrayList<>();

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = new ArrayList<>(usuarios);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return usuarios.size();
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
        Usuario u = usuarios.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> u.getId();
            case 1 -> u.getNombreApellido();
            case 2 -> u.getMail();
            case 3 -> u.getRol().name();
            default -> "";
        };
    }
}
