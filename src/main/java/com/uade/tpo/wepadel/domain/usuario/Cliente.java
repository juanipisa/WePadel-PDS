package com.uade.tpo.wepadel.domain.usuario;

import com.uade.tpo.wepadel.config.ConfiguracionSistema;
import com.uade.tpo.wepadel.domain.carrito.Carrito;
import com.uade.tpo.wepadel.domain.carrito.ItemCarrito;
import com.uade.tpo.wepadel.domain.catalogo.Producto;
import com.uade.tpo.wepadel.domain.notificacion.Notificador;
import com.uade.tpo.wepadel.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.domain.pago.ResultadoPago;
import com.uade.tpo.wepadel.domain.pedido.ItemPedido;
import com.uade.tpo.wepadel.domain.pedido.Pedido;
import com.uade.tpo.wepadel.domain.puntos.SistemaPuntos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

public class Cliente extends Usuario {

    private final Carrito carrito;
    private final SistemaPuntos sistemaPuntos;
    private final List<Pedido> historialPedidos = new ArrayList<>();
    private Notificador preferenciaNotificacion;
    private LongSupplier generadorIdPedido;

    public Cliente(Long id, String nombreApellido, String mail, String password) {
        super(id, nombreApellido, mail, password);
        this.carrito = new Carrito();
        ConfiguracionSistema config = ConfiguracionSistema.getInstancia();
        this.sistemaPuntos = new SistemaPuntos(id, 0, config.getConversionPuntos());
    }

    public void setGeneradorIdPedido(LongSupplier generadorIdPedido) {
        this.generadorIdPedido = generadorIdPedido;
    }

    @Override
    public RolEnum getRol() {
        return RolEnum.CLIENTE;
    }

    public void agregarAlCarrito(Producto producto, int cantidad) {
        carrito.agregarItem(producto, cantidad);
    }

    public int getSaldoPuntos() {
        return sistemaPuntos.getSaldo();
    }

    public BigDecimal canjearPuntos(int cantidad) {
        if (!sistemaPuntos.puedeCanjear(cantidad)) {
            return BigDecimal.ZERO;
        }
        return sistemaPuntos.calcularDescuento(cantidad);
    }

    public Pedido confirmarCompra(MetodoDePago metodoPago, int puntosUsados) {
        if (carrito.estaVacio()) {
            throw new IllegalStateException("El carrito esta vacio");
        }
        if (generadorIdPedido == null) {
            throw new IllegalStateException("Generador de ID de pedido no configurado");
        }

        ConfiguracionSistema config = ConfiguracionSistema.getInstancia();
        BigDecimal subtotal = carrito.calcularTotal();
        BigDecimal descuento = BigDecimal.ZERO;

        if (puntosUsados > 0) {
            if (!sistemaPuntos.puedeCanjear(puntosUsados)) {
                throw new IllegalStateException("Puntos insuficientes para canjear");
            }
            descuento = sistemaPuntos.calcularDescuento(puntosUsados);
        }

        BigDecimal totalAntesDescuento = subtotal.add(config.getCostoEnvio());
        if (descuento.compareTo(totalAntesDescuento) > 0) {
            descuento = totalAntesDescuento;
            puntosUsados = descuento.multiply(BigDecimal.valueOf(sistemaPuntos.getConversion()))
                    .intValue();
        }

        BigDecimal totalAPagar = totalAntesDescuento.subtract(descuento);
        if (totalAPagar.compareTo(BigDecimal.ZERO) < 0) {
            totalAPagar = BigDecimal.ZERO;
        }

        for (ItemCarrito item : carrito.getItems()) {
            item.getProducto().descontarStock(item.getCantidad());
        }

        ResultadoPago resultado = metodoPago.procesarPago(totalAPagar);
        if (!resultado.isExitoso()) {
            for (ItemCarrito item : carrito.getItems()) {
                item.getProducto().reponerStock(item.getCantidad());
            }
            throw new IllegalStateException("Pago rechazado: " + resultado.getMensaje());
        }

        List<ItemPedido> itemsPedido = carrito.getItems().stream()
                .map(item -> new ItemPedido(item.getProducto(), item.getCantidad()))
                .toList();

        int puntosGenerados = calcularPuntosGenerados(totalAPagar, config);

        Pedido pedido = new Pedido(
                generadorIdPedido.getAsLong(),
                this,
                totalAPagar,
                metodoPago,
                puntosUsados > 0,
                puntosUsados,
                puntosGenerados,
                descuento,
                itemsPedido
        );

        if (preferenciaNotificacion != null) {
            pedido.agregarObservador(preferenciaNotificacion);
        }

        pedido.marcarPagado();

        if (puntosUsados > 0) {
            sistemaPuntos.restarPuntos(puntosUsados);
        }
        if (puntosGenerados > 0) {
            sistemaPuntos.sumarPuntos(puntosGenerados);
        }

        historialPedidos.add(pedido);
        carrito.vaciar();
        return pedido;
    }

    public void cancelarPedido(Pedido pedido) {
        if (!historialPedidos.contains(pedido)) {
            throw new IllegalArgumentException("El pedido no pertenece a este cliente");
        }
        if (!pedido.puedeCancelarse()) {
            throw new IllegalStateException("El pedido no puede cancelarse en su estado actual");
        }

        pedido.cancelar();

        for (ItemPedido item : pedido.getItems()) {
            item.getProducto().reponerStock(item.getCantidad());
        }

        if (pedido.isUsaPuntos()) {
            sistemaPuntos.sumarPuntos(pedido.getPuntosUsados());
        }
        if (pedido.getPuntosGenerados() > 0) {
            sistemaPuntos.restarPuntos(pedido.getPuntosGenerados());
        }
    }

    public void setPreferenciaNotificacion(Notificador notificador) {
        this.preferenciaNotificacion = notificador;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public SistemaPuntos getSistemaPuntos() {
        return sistemaPuntos;
    }

    public List<Pedido> getHistorialPedidos() {
        return List.copyOf(historialPedidos);
    }

    public Notificador getPreferenciaNotificacion() {
        return preferenciaNotificacion;
    }

    private int calcularPuntosGenerados(BigDecimal totalPagado, ConfiguracionSistema config) {
        return totalPagado
                .divide(config.getPesosPorPuntoGenerado(), 0, RoundingMode.DOWN)
                .intValue();
    }
}
