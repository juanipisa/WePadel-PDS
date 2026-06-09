package com.uade.tpo.wepadel.backend.domain.puntos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SistemaPuntos {

    private Long id;
    private int cantidad;
    /** Cantidad de puntos necesarios para obtener $1 de descuento. */
    private int conversion;

    public SistemaPuntos(Long id, int cantidad, int conversion) {
        this.id = id;
        this.cantidad = cantidad;
        this.conversion = conversion;
    }

    public int getSaldo() {
        return cantidad;
    }

    public BigDecimal calcularDescuento(int puntosUsados) {
        if (puntosUsados <= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(puntosUsados)
                .divide(BigDecimal.valueOf(conversion), 2, RoundingMode.DOWN);
    }

    public void sumarPuntos(int cantidadPuntos) {
        if (cantidadPuntos <= 0) {
            throw new IllegalArgumentException("La cantidad a sumar debe ser mayor a cero");
        }
        cantidad += cantidadPuntos;
    }

    public void restarPuntos(int cantidadPuntos) {
        if (!puedeCanjear(cantidadPuntos)) {
            throw new PuntosInsuficientesException(
                    "Puntos insuficientes. Saldo: " + cantidad + ", solicitados: " + cantidadPuntos);
        }
        cantidad -= cantidadPuntos;
    }

    public void ajustarPuntos(int cantidadPuntos) {
        int nuevoSaldo = cantidad + cantidadPuntos;
        if (nuevoSaldo < 0) {
            throw new PuntosNegativosException("El saldo de puntos no puede quedar negativo");
        }
        cantidad = nuevoSaldo;
    }

    public boolean puedeCanjear(int puntosUsados) {
        return puntosUsados > 0 && cantidad >= puntosUsados;
    }

    public Long getId() {
        return id;
    }

    public int getConversion() {
        return conversion;
    }
}
