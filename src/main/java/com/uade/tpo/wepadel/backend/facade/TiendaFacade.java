package com.uade.tpo.wepadel.backend.facade;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.config.ConfiguracionSistema;
import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosAccesorio;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosCalzado;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosPaleta;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosPelota;
import com.uade.tpo.wepadel.backend.domain.catalogo.Paleta;
import com.uade.tpo.wepadel.backend.domain.catalogo.Pelota;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.backend.domain.notificacion.Notificador;
import com.uade.tpo.wepadel.backend.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.backend.domain.pedido.Pedido;
import com.uade.tpo.wepadel.backend.domain.usuario.Administrador;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.backend.domain.usuario.RolEnum;
import com.uade.tpo.wepadel.backend.domain.usuario.Usuario;
import com.uade.tpo.wepadel.persistencia.service.CarritoService;
import com.uade.tpo.wepadel.persistencia.service.CatalogoService;
import com.uade.tpo.wepadel.persistencia.service.ConfiguracionService;
import com.uade.tpo.wepadel.persistencia.service.PedidoService;
import com.uade.tpo.wepadel.persistencia.service.ProductoService;
import com.uade.tpo.wepadel.persistencia.service.PuntosService;
import com.uade.tpo.wepadel.persistencia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

/**
 * Patron FACADE: punto unico de acceso a la tienda.
 * Orquesta el dominio (backend) y delega el guardado en BD a persistencia.
 */
@Service
@RequiredArgsConstructor
public class TiendaFacade {

    private final UsuarioService usuarioService;
    private final CatalogoService catalogoService;
    private final CarritoService carritoService;
    private final PedidoService pedidoService;
    private final PuntosService puntosService;
    private final ConfiguracionService configuracionService;
    private final ProductoService productoService;

    @Transactional
    public Usuario registrarUsuario(String nombreApellido, String mail, String password, RolEnum rol) {
        return usuarioService.registrarUsuario(nombreApellido, mail, password, rol);
    }

    @Transactional(readOnly = true)
    public Usuario login(String mail, String pass) {
        return usuarioService.login(mail, pass);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarProductos(String filtro) {
        return catalogoService.buscarProductos(filtro);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            return List.of();
        }
        return categoria.getProductos();
    }

    @Transactional
    public void agregarAlCarrito(Cliente cliente, Producto producto, int cantidad) {
        cliente.agregarAlCarrito(producto, cantidad);
        carritoService.sincronizarCarrito(cliente);
    }

    @Transactional
    public void modificarCantidadCarrito(Cliente cliente, Producto producto, int cantidad) {
        cliente.getCarrito().modificarCantidad(producto, cantidad);
        carritoService.sincronizarCarrito(cliente);
    }

    @Transactional
    public void eliminarDelCarrito(Cliente cliente, Producto producto) {
        cliente.getCarrito().eliminarItem(producto);
        carritoService.sincronizarCarrito(cliente);
    }

    @Transactional(readOnly = true)
    public int consultarSaldoPuntos(Cliente cliente) {
        return cliente.getSaldoPuntos();
    }

    /** Dominio: Cliente aplica reglas. Persistencia: solo guarda el resultado. */
    @Transactional
    public Pedido confirmarCompra(Cliente cliente, MetodoDePago metodoPago, int puntosUsados) {
        Pedido pedido = cliente.confirmarCompra(metodoPago, puntosUsados);
        return pedidoService.guardarPedido(pedido, cliente, metodoPago);
    }

    @Transactional
    public void cancelarPedido(Pedido pedidoReferencia) {
        Cliente cliente = usuarioService.obtenerCliente(pedidoReferencia.getCliente().getId());
        pedidoService.cargarHistorialEnCliente(cliente);
        Pedido pedido = cliente.getHistorialPedidos().stream()
                .filter(p -> p.getId().equals(pedidoReferencia.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado para el cliente"));
        cliente.cancelarPedido(pedido);
        pedidoService.sincronizarCancelacion(pedido, cliente);
    }

    @Transactional
    public void cambiarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoService.buscarPedido(pedidoId);
        Administrador admin = usuarioService.obtenerAdministrador();
        admin.cambiarEstadoPedido(pedido, nuevoEstado);
        pedidoService.actualizarEstado(pedido, nuevoEstado);
    }

    @Transactional
    public void cambiarEstadoPedido(Administrador admin, Pedido pedido, String nuevoEstado) {
        admin.cambiarEstadoPedido(pedido, nuevoEstado);
        pedidoService.actualizarEstado(pedido, nuevoEstado);
    }

    @Transactional(readOnly = true)
    public Categoria getCatalogoRaiz() {
        return catalogoService.getCatalogoRaiz();
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidos() {
        return pedidoService.listarPedidos();
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidosCliente(Cliente cliente) {
        return pedidoService.listarPedidosPorCliente(cliente.getId());
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @Transactional(readOnly = true)
    public Administrador obtenerAdministrador() {
        return usuarioService.obtenerAdministrador();
    }

    public ConfiguracionSistema getConfiguracion() {
        return configuracionService.getConfiguracion();
    }

    @Transactional
    public void configurarNotificacion(Cliente cliente, Notificador notificador) {
        usuarioService.configurarNotificacion(cliente, notificador);
    }

    @Transactional
    public Categoria crearCategoria(Administrador admin, String nombre, Categoria padre) {
        return catalogoService.crearCategoria(nombre, padre);
    }

    @Transactional
    public void actualizarStock(Administrador admin, Producto producto, int cantidad) {
        admin.actualizarStock(producto, cantidad);
        productoService.sincronizarStock(producto);
    }

    @Transactional
    public void cambiarHabilitadoProducto(Administrador admin, Producto producto, boolean habilitado) {
        producto.setHabilitado(habilitado);
        productoService.sincronizarHabilitado(producto);
    }

    @Transactional
    public void guardarConfiguracion(BigDecimal costoEnvio, int conversionPuntos, BigDecimal pesosPorPuntoGenerado,
                                       String canalNotificacionDefault) {
        configuracionService.guardarConfiguracion(costoEnvio, conversionPuntos, pesosPorPuntoGenerado,
                canalNotificacionDefault);
    }

    @Transactional(readOnly = true)
    public Categoria refrescarCatalogo() {
        return catalogoService.reconstruirCatalogo();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodosLosProductos() {
        return catalogoService.listarTodosLosProductos();
    }

    @Transactional
    public Paleta crearPaleta(Administrador admin, DatosPaleta datos) {
        return (Paleta) catalogoService.guardarPaleta(datos);
    }

    @Transactional
    public Pelota crearPelota(Administrador admin, DatosPelota datos) {
        return (Pelota) catalogoService.guardarPelota(datos);
    }

    @Transactional
    public Producto crearAccesorio(Administrador admin, DatosAccesorio datos) {
        return catalogoService.guardarAccesorio(datos);
    }

    @Transactional
    public Producto crearCalzado(Administrador admin, DatosCalzado datos) {
        return catalogoService.guardarCalzado(datos);
    }

    @Transactional
    public void sumarPuntos(Cliente cliente, int puntos) {
        cliente.getSistemaPuntos().sumarPuntos(puntos);
        puntosService.sincronizarPuntos(cliente);
    }
}
