package com.uade.tpo.wepadel.persistencia.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.wepadel.backend.domain.catalogo.Accesorio;
import com.uade.tpo.wepadel.backend.domain.catalogo.Calzado;
import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.Paleta;
import com.uade.tpo.wepadel.backend.domain.catalogo.Pelota;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.backend.domain.notificacion.Notificador;
import com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorEmail;
import com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorPush;
import com.uade.tpo.wepadel.backend.domain.notificacion.NotificadorSMS;
import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePagoFactory;
import com.uade.tpo.wepadel.backend.domain.pago.TipoMetodoPago;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.backend.domain.pedido.estado.EstadoPedidoFactory;
import com.uade.tpo.wepadel.backend.domain.usuario.Administrador;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.persistencia.entity.CanalNotificacionEnum;
import com.uade.tpo.wepadel.persistencia.entity.EstadoPedidoEnum;
import com.uade.tpo.wepadel.persistencia.entity.ItemPedido;
import com.uade.tpo.wepadel.persistencia.entity.RolEnum;
import com.uade.tpo.wepadel.persistencia.entity.TipoPagoEnum;
import com.uade.tpo.wepadel.persistencia.entity.Usuario;

/** Traduce entre entidades JPA (persistencia) y objetos de dominio (backend). */
@Component
public class DomainMapper {

    public Producto toDomainProducto(com.uade.tpo.wepadel.persistencia.entity.Producto entity, Categoria categoriaDomain) {
        if (entity instanceof com.uade.tpo.wepadel.persistencia.entity.Paleta paleta) {
            return new Paleta(
                    paleta.getId(),
                    paleta.getNombre(),
                    paleta.getDescripcion(),
                    paleta.getPrecio(),
                    paleta.getStock(),
                    paleta.isHabilitado(),
                    categoriaDomain,
                    paleta.getPesoGramos(),
                    paleta.getBalance(),
                    paleta.getForma(),
                    paleta.getMaterial()
            );
        }
        if (entity instanceof com.uade.tpo.wepadel.persistencia.entity.Pelota pelota) {
            return new Pelota(
                    pelota.getId(),
                    pelota.getNombre(),
                    pelota.getDescripcion(),
                    pelota.getPrecio(),
                    pelota.getStock(),
                    pelota.isHabilitado(),
                    categoriaDomain,
                    pelota.getPresion(),
                    pelota.getUnidadesPorTubo()
            );
        }
        if (entity instanceof com.uade.tpo.wepadel.persistencia.entity.Accesorio accesorio) {
            return new Accesorio(
                    accesorio.getId(),
                    accesorio.getNombre(),
                    accesorio.getDescripcion(),
                    accesorio.getPrecio(),
                    accesorio.getStock(),
                    accesorio.isHabilitado(),
                    categoriaDomain,
                    accesorio.getTipo(),
                    accesorio.getMaterial()
            );
        }
        if (entity instanceof com.uade.tpo.wepadel.persistencia.entity.Calzado calzado) {
            return new Calzado(
                    calzado.getId(),
                    calzado.getNombre(),
                    calzado.getDescripcion(),
                    calzado.getPrecio(),
                    calzado.getStock(),
                    calzado.isHabilitado(),
                    categoriaDomain,
                    calzado.getTalle(),
                    calzado.getColor(),
                    calzado.getGenero()
            );
        }
        throw new IllegalStateException("Tipo de producto no soportado: " + entity.getClass().getSimpleName());
    }

    public com.uade.tpo.wepadel.backend.domain.usuario.Usuario toDomainUsuarioBasico(Usuario entity) {
        if (entity.getRol() == RolEnum.ADMINISTRADOR) {
            return new Administrador(
                    entity.getId(),
                    entity.getNombreApellido(),
                    entity.getMail(),
                    entity.getPassword()
            );
        }
        return new Cliente(
                entity.getId(),
                entity.getNombreApellido(),
                entity.getMail(),
                entity.getPassword()
        );
    }

    public Notificador toNotificador(CanalNotificacionEnum canal, Cliente cliente) {
        if (canal == null) {
            return null;
        }
        return switch (canal) {
            case EMAIL -> new NotificadorEmail(cliente);
            case SMS -> new NotificadorSMS(cliente);
            case PUSH -> new NotificadorPush(cliente);
        };
    }

    public TipoPagoEnum toTipoPago(MetodoDePago metodoPago) {
        TipoMetodoPago tipo = MetodoDePagoFactory.resolverTipo(metodoPago);
        return TipoPagoEnum.valueOf(tipo.name());
    }

    public com.uade.tpo.wepadel.persistencia.entity.Pedido toEntityPedido(
            Pedido pedidoDomain,
            Usuario clienteEntity,
            MetodoDePago metodoPago,
            String codigoTransaccion,
            java.math.BigDecimal costoEnvio) {

        com.uade.tpo.wepadel.persistencia.entity.Pedido pedidoEntity = new com.uade.tpo.wepadel.persistencia.entity.Pedido();
        pedidoEntity.setCliente(clienteEntity);
        pedidoEntity.setTotal(pedidoDomain.getTotal());
        pedidoEntity.setCostoEnvio(costoEnvio);
        pedidoEntity.setDescuentoPorPuntos(pedidoDomain.getDescuentoPorPuntos());
        pedidoEntity.setFechaCompra(pedidoDomain.getFechaCompra());
        pedidoEntity.setEstado(EstadoPedidoEnum.valueOf(pedidoDomain.getEstado().getNombre()));
        pedidoEntity.setTipoPago(toTipoPago(metodoPago));
        pedidoEntity.setCodigoTransaccion(codigoTransaccion);
        pedidoEntity.setUsaPuntos(pedidoDomain.isUsaPuntos());
        pedidoEntity.setPuntosUsados(pedidoDomain.getPuntosUsados());
        pedidoEntity.setPuntosGenerados(pedidoDomain.getPuntosGenerados());

        for (com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido itemDomain : pedidoDomain.getItems()) {
            ItemPedido itemEntity = new ItemPedido(
                    pedidoEntity,
                    null,
                    itemDomain.getCantidad(),
                    itemDomain.getPrecioUnitarioHistorico(),
                    itemDomain.getDescripcionProducto()
            );
            pedidoEntity.getItems().add(itemEntity);
        }

        return pedidoEntity;
    }

    public Pedido toDomainPedido(com.uade.tpo.wepadel.persistencia.entity.Pedido entity, Cliente cliente,
                                 java.util.Map<Long, Producto> productosPorId) {
        java.util.List<com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido> items = entity.getItems().stream()
                .map(item -> {
                    Producto producto = item.getProducto() != null
                            ? productosPorId.get(item.getProducto().getId())
                            : null;
                    if (producto != null) {
                        return new com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido(producto, item.getCantidad());
                    }
                    return crearItemPedidoDesdeSnapshot(item);
                })
                .toList();

        Pedido pedido = new Pedido(
                entity.getId(),
                cliente,
                entity.getTotal(),
                MetodoDePagoFactory.reconstruirStub(TipoMetodoPago.valueOf(entity.getTipoPago().name())),
                entity.isUsaPuntos(),
                entity.getPuntosUsados(),
                entity.getPuntosGenerados(),
                entity.getDescuentoPorPuntos(),
                items
        );
        pedido.cambiarEstado(EstadoPedidoFactory.fromNombre(entity.getEstado().name()));
        return pedido;
    }

    private com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido crearItemPedidoDesdeSnapshot(ItemPedido item) {
        Producto productoStub = new ProductoStub(
                item.getProducto() != null ? item.getProducto().getId() : null,
                item.getDescripcionProducto(),
                item.getPrecioUnitarioHistorico()
        );
        return new com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido(productoStub, item.getCantidad());
    }

    private static final class ProductoStub extends Producto {
        private final String descripcionTecnica;

        ProductoStub(Long id, String descripcionTecnica, java.math.BigDecimal precio) {
            super(id, descripcionTecnica, "", precio, 0, false, new Categoria("Historico"));
            this.descripcionTecnica = descripcionTecnica;
        }

        @Override
        public String getDescripcionTecnica() {
            return descripcionTecnica;
        }
    }
}
