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
@Table(name = "accesorio")
@DiscriminatorValue("ACCESORIO")
@PrimaryKeyJoinColumn(name = "id")
public class Accesorio extends Producto {

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "material")
    private String material;

    public Accesorio(String nombre, String descripcion, BigDecimal precio, int stock,
                     Categoria categoria, String tipo, String material) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setStock(stock);
        setHabilitado(true);
        setCategoria(categoria);
        this.tipo = tipo;
        this.material = material;
    }
}
