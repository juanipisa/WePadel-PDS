package com.uade.tpo.wepadel.domain.pedido.estado;

public final class EstadoPedidoFactory {

    private EstadoPedidoFactory() {
    }

    public static EstadoPedido fromNombre(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre del estado no puede ser nulo");
        }
        return switch (nombre.toUpperCase()) {
            case "PENDIENTE" -> new EstadoPendiente();
            case "PAGADO" -> new EstadoPagado();
            case "ENVIADO" -> new EstadoEnviado();
            case "ENTREGADO" -> new EstadoEntregado();
            case "CANCELADO" -> new EstadoCancelado();
            default -> throw new IllegalArgumentException("Estado de pedido desconocido: " + nombre);
        };
    }
}
