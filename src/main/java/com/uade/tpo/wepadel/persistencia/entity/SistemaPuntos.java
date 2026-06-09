package com.uade.tpo.wepadel.persistencia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sistema_puntos")
public class SistemaPuntos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "cantidad", nullable = false)
    private int cantidad = 0;

    @Column(name = "conversion", nullable = false)
    private int conversion = 100;

    public SistemaPuntos(Usuario usuario, int conversion) {
        this.usuario = usuario;
        this.conversion = conversion;
    }
}
