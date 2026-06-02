package com.uade.tpo.wepadel.facade;

import com.uade.tpo.wepadel.config.ConfiguracionSistema;
import com.uade.tpo.wepadel.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.domain.catalogo.Producto;
import com.uade.tpo.wepadel.domain.notificacion.Notificador;
import com.uade.tpo.wepadel.domain.pago.MetodoDePago;
import com.uade.tpo.wepadel.domain.pedido.Pedido;
import com.uade.tpo.wepadel.domain.usuario.Administrador;
import com.uade.tpo.wepadel.domain.usuario.Cliente;
import com.uade.tpo.wepadel.domain.usuario.RolEnum;
import com.uade.tpo.wepadel.domain.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TiendaFacade {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private Categoria catalogoRaiz;

    private final AtomicLong secuenciaUsuario = new AtomicLong(1);
    private final AtomicLong secuenciaProducto = new AtomicLong(1);
    private final AtomicLong secuenciaPedido = new AtomicLong(1);

    public TiendaFacade() {
        this.catalogoRaiz = new Categoria("WePadel");
    }

    public Usuario registrarUsuario(String nombreApellido, String mail, String password, RolEnum rol) {
        if (buscarPorMail(mail) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese mail");
        }

        Long id = secuenciaUsuario.getAndIncrement();
        Usuario usuario = switch (rol) {
            case CLIENTE -> {
                Cliente cliente = new Cliente(id, nombreApellido, mail, password);
                cliente.setGeneradorIdPedido(secuenciaPedido::getAndIncrement);
                yield cliente;
            }
            case ADMINISTRADOR -> {
                Administrador admin = new Administrador(id, nombreApellido, mail, password);
                admin.setGeneradorIdProducto(secuenciaProducto::getAndIncrement);
                yield admin;
            }
        };

        usuarios.add(usuario);
        return usuario;
    }

    public Usuario login(String mail, String pass) {
        Usuario usuario = buscarPorMail(mail);
        if (usuario == null || !usuario.validarCredenciales(pass)) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }
        return usuario;
    }

    public List<Producto> buscarProductos(String filtro) {
        List<Producto> todos = catalogoRaiz.getProductos();
        if (filtro == null || filtro.isBlank()) {
            return todos;
        }
        String filtroLower = filtro.toLowerCase();
        return todos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtroLower)
                        || p.getDescripcion().toLowerCase().contains(filtroLower)
                        || p.getDescripcionTecnica().toLowerCase().contains(filtroLower))
                .toList();
    }

    public List<Producto> buscarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            return List.of();
        }
        return categoria.getProductos();
    }

    public void agregarAlCarrito(Cliente cliente, Producto producto, int cantidad) {
        cliente.agregarAlCarrito(producto, cantidad);
    }

    public int consultarSaldoPuntos(Cliente cliente) {
        return cliente.getSaldoPuntos();
    }

    public Pedido confirmarCompra(Cliente cliente, MetodoDePago metodoPago, int puntosUsados) {
        Pedido pedido = cliente.confirmarCompra(metodoPago, puntosUsados);
        pedidos.add(pedido);
        return pedido;
    }

    public void cancelarPedido(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        cliente.cancelarPedido(pedido);
    }

    public void cambiarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = buscarPedido(pedidoId);
        Administrador admin = usuarios.stream()
                .filter(u -> u instanceof Administrador)
                .map(u -> (Administrador) u)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay administrador registrado"));
        admin.cambiarEstadoPedido(pedido, nuevoEstado);
    }

    public void cambiarEstadoPedido(Administrador admin, Pedido pedido, String nuevoEstado) {
        admin.cambiarEstadoPedido(pedido, nuevoEstado);
    }

    public Categoria getCatalogoRaiz() {
        return catalogoRaiz;
    }

    public void setCatalogoRaiz(Categoria catalogoRaiz) {
        this.catalogoRaiz = catalogoRaiz;
    }

    public List<Pedido> getPedidos() {
        return List.copyOf(pedidos);
    }

    public List<Usuario> getUsuarios() {
        return List.copyOf(usuarios);
    }

    public Administrador obtenerAdministrador() {
        return usuarios.stream()
                .filter(u -> u instanceof Administrador)
                .map(u -> (Administrador) u)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay administrador registrado"));
    }

    public ConfiguracionSistema getConfiguracion() {
        return ConfiguracionSistema.getInstancia();
    }

    public void configurarNotificacion(Cliente cliente, Notificador notificador) {
        cliente.setPreferenciaNotificacion(notificador);
    }

    private Usuario buscarPorMail(String mail) {
        return usuarios.stream()
                .filter(u -> u.getMail().equalsIgnoreCase(mail))
                .findFirst()
                .orElse(null);
    }

    private Pedido buscarPedido(Long pedidoId) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(pedidoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));
    }
}
