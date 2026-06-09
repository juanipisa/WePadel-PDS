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
@Table(name = "calzado")
@DiscriminatorValue("CALZADO")
@PrimaryKeyJoinColumn(name = "id")
public class Calzado extends Producto {

    @Column(name = "talle")
    private int talle;

    @Column(name = "color")
    private String color;

    @Column(name = "genero")
    private String genero;

    public Calzado(String nombre, String descripcion, BigDecimal precio, int stock,
                   Categoria categoria, int talle, String color, String genero) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setStock(stock);
        setHabilitado(true);
        setCategoria(categoria);
        this.talle = talle;
        this.color = color;
        this.genero = genero;
    }
}
