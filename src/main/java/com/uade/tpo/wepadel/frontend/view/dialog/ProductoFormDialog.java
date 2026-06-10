package com.uade.tpo.wepadel.frontend.view.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.uade.tpo.wepadel.frontend.util.CategoriaUtil.CategoriaItem;

public class ProductoFormDialog extends JDialog {

    private final JTextField nombreField = new JTextField(20);
    private final JTextField descripcionField = new JTextField(20);
    private final JTextField precioField = new JTextField(10);
    private final JTextField stockField = new JTextField(5);
    private final JComboBox<CategoriaItem> categoriaCombo = new JComboBox<>();
    private final JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Paleta", "Pelota", "Accesorio", "Calzado"});

    private final JPanel camposEspecificos = new JPanel(new CardLayout());
    private final JTextField pesoField = new JTextField(5);
    private final JTextField balanceField = new JTextField(10);
    private final JTextField formaField = new JTextField(10);
    private final JTextField materialPaletaField = new JTextField(10);
    private final JTextField presionField = new JTextField(10);
    private final JTextField unidadesField = new JTextField(5);
    private final JTextField tipoAccesorioField = new JTextField(10);
    private final JTextField materialAccField = new JTextField(10);
    private final JTextField talleField = new JTextField(5);
    private final JTextField colorField = new JTextField(10);
    private final JTextField generoField = new JTextField(10);

    private final JButton crearButton = new JButton("Crear");
    private final JButton cancelarButton = new JButton("Cancelar");
    private boolean creado;

    public ProductoFormDialog(Window owner) {
        super(owner, "Nuevo producto", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        JPanel comunes = new JPanel(new GridLayout(6, 2, 5, 5));
        comunes.add(new JLabel("Nombre:"));
        comunes.add(nombreField);
        comunes.add(new JLabel("Descripcion:"));
        comunes.add(descripcionField);
        comunes.add(new JLabel("Precio:"));
        comunes.add(precioField);
        comunes.add(new JLabel("Stock:"));
        comunes.add(stockField);
        comunes.add(new JLabel("Categoria:"));
        comunes.add(categoriaCombo);
        comunes.add(new JLabel("Tipo:"));
        comunes.add(tipoCombo);

        camposEspecificos.add(buildPaletaPanel(), "Paleta");
        camposEspecificos.add(buildPelotaPanel(), "Pelota");
        camposEspecificos.add(buildAccesorioPanel(), "Accesorio");
        camposEspecificos.add(buildCalzadoPanel(), "Calzado");
        ((CardLayout) camposEspecificos.getLayout()).show(camposEspecificos, "Paleta");

        JPanel center = new JPanel(new BorderLayout());
        center.add(comunes, BorderLayout.NORTH);
        center.add(camposEspecificos, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(crearButton);
        buttons.add(cancelarButton);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel buildPaletaPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
        p.add(new JLabel("Peso (g):"));
        p.add(pesoField);
        p.add(new JLabel("Balance:"));
        p.add(balanceField);
        p.add(new JLabel("Forma:"));
        p.add(formaField);
        p.add(new JLabel("Material:"));
        p.add(materialPaletaField);
        return p;
    }

    private JPanel buildPelotaPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        p.add(new JLabel("Presion:"));
        p.add(presionField);
        p.add(new JLabel("Unidades por tubo:"));
        p.add(unidadesField);
        return p;
    }

    private JPanel buildAccesorioPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        p.add(new JLabel("Tipo:"));
        p.add(tipoAccesorioField);
        p.add(new JLabel("Material:"));
        p.add(materialAccField);
        return p;
    }

    private JPanel buildCalzadoPanel() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.add(new JLabel("Talle:"));
        p.add(talleField);
        p.add(new JLabel("Color:"));
        p.add(colorField);
        p.add(new JLabel("Genero:"));
        p.add(generoField);
        return p;
    }

    public JTextField getNombreField() {
        return nombreField;
    }

    public JTextField getDescripcionField() {
        return descripcionField;
    }

    public JTextField getPrecioField() {
        return precioField;
    }

    public JTextField getStockField() {
        return stockField;
    }

    public JComboBox<CategoriaItem> getCategoriaCombo() {
        return categoriaCombo;
    }

    public JComboBox<String> getTipoCombo() {
        return tipoCombo;
    }

    public JPanel getCamposEspecificos() {
        return camposEspecificos;
    }

    public JTextField getPesoField() {
        return pesoField;
    }

    public JTextField getBalanceField() {
        return balanceField;
    }

    public JTextField getFormaField() {
        return formaField;
    }

    public JTextField getMaterialPaletaField() {
        return materialPaletaField;
    }

    public JTextField getPresionField() {
        return presionField;
    }

    public JTextField getUnidadesField() {
        return unidadesField;
    }

    public JTextField getTipoAccesorioField() {
        return tipoAccesorioField;
    }

    public JTextField getMaterialAccField() {
        return materialAccField;
    }

    public JTextField getTalleField() {
        return talleField;
    }

    public JTextField getColorField() {
        return colorField;
    }

    public JTextField getGeneroField() {
        return generoField;
    }

    public JButton getCrearButton() {
        return crearButton;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public boolean isCreado() {
        return creado;
    }

    public void setCreado(boolean creado) {
        this.creado = creado;
    }
}
