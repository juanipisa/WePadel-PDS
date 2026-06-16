package com.uade.tpo.wepadel.persistencia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.persistencia.entity.EstadoPedidoEnum;
import com.uade.tpo.wepadel.persistencia.entity.ItemPedido;
import com.uade.tpo.wepadel.persistencia.entity.Usuario;
import com.uade.tpo.wepadel.persistencia.mapper.DomainMapper;
import com.uade.tpo.wepadel.persistencia.repository.PedidoRepository;
import com.uade.tpo.wepadel.persistencia.repository.ProductoRepository;
import com.uade.tpo.wepadel.persistencia.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/** Persistencia: guarda pedidos en MySQL. Las reglas estan en backend.domain. */
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final DomainMapper domainMapper;
    private final ProductoService productoService;
    private final CarritoService carritoService;
    private final PuntosService puntosService;
    private final ConfiguracionService configuracionService;
    @Lazy
    private final CatalogoService catalogoService;

    @Transactional
    public Pedido guardarPedido(Pedido pedidoDomain, Cliente cliente, MetodoDePago metodoPago) {
        Usuario clienteEntity = usuarioRepository.getReferenceById(cliente.getId());
        BigDecimal costoEnvio = configuracionService.getConfiguracion().getCostoEnvio();

        com.uade.tpo.wepadel.persistencia.entity.Pedido pedidoEntity = domainMapper.toEntityPedido(
                pedidoDomain, clienteEntity, metodoPago, pedidoDomain.getCodigoTransaccion(), costoEnvio);

        for (int i = 0; i < pedidoDomain.getItems().size(); i++) {
            com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido itemDomain = pedidoDomain.getItems().get(i);
            ItemPedido itemEntity = pedidoEntity.getItems().get(i);
            itemEntity.setProducto(productoRepository.getReferenceById(itemDomain.getProducto().getId()));
        }

        pedidoEntity = pedidoRepository.save(pedidoEntity);
        pedidoDomain.setId(pedidoEntity.getId());

        for (com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido item : pedidoDomain.getItems()) {
            productoService.sincronizarStock(item.getProducto());
        }

        puntosService.sincronizarPuntos(cliente);
        carritoService.vaciarCarrito(cliente);
        return pedidoDomain;
    }

    @Transactional
    public void actualizarEstado(Pedido pedidoDomain, String nuevoEstado) {
        com.uade.tpo.wepadel.persistencia.entity.Pedido pedidoEntity = pedidoRepository.findById(pedidoDomain.getId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoDomain.getId()));
        pedidoEntity.setEstado(EstadoPedidoEnum.valueOf(nuevoEstado.toUpperCase()));
        pedidoRepository.save(pedidoEntity);
    }

    @Transactional
    public void sincronizarCancelacion(Pedido pedido, Cliente cliente) {
        for (com.uade.tpo.wepadel.backend.domain.pedido.ItemPedido item : pedido.getItems()) {
            productoService.sincronizarStock(item.getProducto());
        }
        puntosService.sincronizarPuntos(cliente);
        actualizarEstado(pedido, pedido.getEstado().getNombre());
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        var productosPorId = catalogoService.getProductosPorId();

        for (com.uade.tpo.wepadel.persistencia.entity.Pedido pedidoEntity : pedidoRepository.findAll()) {
            Cliente cliente = (Cliente) domainMapper.toDomainUsuarioBasico(pedidoEntity.getCliente());
            cliente.setGeneradorIdPedido(() -> pedidoEntity.getId());
            pedidos.add(domainMapper.toDomainPedido(pedidoEntity, cliente, productosPorId));
        }
        return pedidos;
    }

    @Transactional(readOnly = true)
    public Pedido buscarPedido(Long pedidoId) {
        com.uade.tpo.wepadel.persistencia.entity.Pedido pedidoEntity = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));
        Cliente cliente = (Cliente) domainMapper.toDomainUsuarioBasico(pedidoEntity.getCliente());
        return domainMapper.toDomainPedido(pedidoEntity, cliente, catalogoService.getProductosPorId());
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorCliente(Long clienteId) {
        List<Pedido> pedidos = new ArrayList<>();
        var productosPorId = catalogoService.getProductosPorId();
        Usuario clienteEntity = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));
        Cliente cliente = (Cliente) domainMapper.toDomainUsuarioBasico(clienteEntity);

        for (com.uade.tpo.wepadel.persistencia.entity.Pedido pedidoEntity : pedidoRepository.findByClienteId(clienteId)) {
            pedidos.add(domainMapper.toDomainPedido(pedidoEntity, cliente, productosPorId));
        }
        return pedidos;
    }

    @Transactional(readOnly = true)
    public void cargarHistorialEnCliente(Cliente cliente) {
        cliente.cargarHistorialPedidos(listarPedidosPorCliente(cliente.getId()));
    }

}
