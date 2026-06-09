package com.uade.tpo.wepadel.persistencia.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "paleta")
@DiscriminatorValue("PALETA")
@PrimaryKeyJoinColumn(name = "id")
public class Paleta extends Producto {

    @Column(name = "peso_gramos")
    private int pesoGramos;

    @Column(name = "balance")
    private String balance;

    @Column(name = "forma")
    private String forma;

    @Column(name = "material")
    private String material;

    public Paleta(String nombre, String descripcion, BigDecimal precio, int stock,
                    Categoria categoria, int pesoGramos, String balance, String forma, String material) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setStock(stock);
        setHabilitado(true);
        setCategoria(categoria);
        this.pesoGramos = pesoGramos;
        this.balance = balance;
        this.forma = forma;
        this.material = material;
    }
}
