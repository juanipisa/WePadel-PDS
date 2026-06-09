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
@Table(name = "pelota")
@DiscriminatorValue("PELOTA")
@PrimaryKeyJoinColumn(name = "id")
public class Pelota extends Producto {

    @Column(name = "presion")
    private String presion;

    @Column(name = "unidades_por_tubo")
    private int unidadesPorTubo;

    public Pelota(String nombre, String descripcion, BigDecimal precio, int stock,
                  Categoria categoria, String presion, int unidadesPorTubo) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setStock(stock);
        setHabilitado(true);
        setCategoria(categoria);
        this.presion = presion;
        this.unidadesPorTubo = unidadesPorTubo;
    }
}
