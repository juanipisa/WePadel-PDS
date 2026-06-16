package com.uade.tpo.wepadel.backend.domain.pedido;

import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pedido.estado.EstadoPedido;
import com.uade.tpo.wepadel.backend.domain.pedido.estado.EstadoPendiente;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Agregado de pedido. Usa STATE (estado) y OBSERVER (observadores al cambiar estado).
 */
public class Pedido {

    private Long id;
    private Cliente cliente;
    private BigDecimal total;
    private LocalDateTime fechaCompra;
    private boolean usaPuntos;
    private int puntosUsados;
    private int puntosGenerados;
    private BigDecimal descuentoPorPuntos;
    private EstadoPedido estado;
    private MetodoDePago metodoPago;
    private String codigoTransaccion;
    private final List<ItemPedido> items = new ArrayList<>();
    private final List<Observador> observadores = new ArrayList<>();

    public Pedido(Long id, Cliente cliente, BigDecimal total, MetodoDePago metodoPago,
                  boolean usaPuntos, int puntosUsados, int puntosGenerados,
                  BigDecimal descuentoPorPuntos, List<ItemPedido> itemsPedido) {
        this.id = id;
        this.cliente = cliente;
        this.total = total;
        this.fechaCompra = LocalDateTime.now();
        this.metodoPago = metodoPago;
        this.usaPuntos = usaPuntos;
        this.puntosUsados = puntosUsados;
        this.puntosGenerados = puntosGenerados;
        this.descuentoPorPuntos = descuentoPorPuntos;
        this.estado = new EstadoPendiente();
        this.items.addAll(itemsPedido);
    }

    public void confirmar() {
        estado.confirmar(this);
    }

    public void marcarPagado() {
        estado.marcarPagado(this);
    }

    public void enviar() {
        estado.enviar(this);
    }

    public void entregar() {
        estado.entregar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    public void agregarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void notificar(EstadoPedido estadoAnterior, EstadoPedido estadoNuevo) {
        for (Observador observador : observadores) {
            observador.actualizar(this, estadoAnterior, estadoNuevo);
        }
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        EstadoPedido estadoAnterior = this.estado;
        this.estado = nuevoEstado;
        notificar(estadoAnterior, nuevoEstado);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public boolean isUsaPuntos() {
        return usaPuntos;
    }

    public int getPuntosUsados() {
        return puntosUsados;
    }

    public int getPuntosGenerados() {
        return puntosGenerados;
    }

    public BigDecimal getDescuentoPorPuntos() {
        return descuentoPorPuntos;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public MetodoDePago getMetodoPago() {
        return metodoPago;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public List<ItemPedido> getItems() {
        return List.copyOf(items);
    }

    public boolean puedeCancelarse() {
        String nombre = estado.getNombre();
        return "PENDIENTE".equals(nombre) || "PAGADO".equals(nombre);
    }
}
