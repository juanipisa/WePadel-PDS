package com.uade.tpo.wepadel.persistencia.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario_historico", nullable = false)
    private BigDecimal precioUnitarioHistorico;

    @Column(name = "descripcion_producto", nullable = false)
    private String descripcionProducto;

    public ItemPedido(Pedido pedido, Producto producto, int cantidad,
                      BigDecimal precioUnitarioHistorico, String descripcionProducto) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
        this.descripcionProducto = descripcionProducto;
    }
}
