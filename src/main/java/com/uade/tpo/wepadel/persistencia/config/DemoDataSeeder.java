package com.uade.tpo.wepadel.persistencia.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.persistencia.entity.Accesorio;
import com.uade.tpo.wepadel.persistencia.entity.Calzado;
import com.uade.tpo.wepadel.persistencia.entity.CanalNotificacionEnum;
import com.uade.tpo.wepadel.persistencia.entity.Carrito;
import com.uade.tpo.wepadel.persistencia.entity.CarritoItem;
import com.uade.tpo.wepadel.persistencia.entity.Categoria;
import com.uade.tpo.wepadel.persistencia.entity.ConfiguracionSistema;
import com.uade.tpo.wepadel.persistencia.entity.EstadoPedidoEnum;
import com.uade.tpo.wepadel.persistencia.entity.ItemPedido;
import com.uade.tpo.wepadel.persistencia.entity.Paleta;
import com.uade.tpo.wepadel.persistencia.entity.Pedido;
import com.uade.tpo.wepadel.persistencia.entity.Pelota;
import com.uade.tpo.wepadel.persistencia.entity.Producto;
import com.uade.tpo.wepadel.persistencia.entity.RolEnum;
import com.uade.tpo.wepadel.persistencia.entity.SistemaPuntos;
import com.uade.tpo.wepadel.persistencia.entity.TipoPagoEnum;
import com.uade.tpo.wepadel.persistencia.entity.Usuario;
import com.uade.tpo.wepadel.persistencia.repository.CarritoRepository;
import com.uade.tpo.wepadel.persistencia.repository.CategoriaRepository;
import com.uade.tpo.wepadel.persistencia.repository.ConfiguracionSistemaRepository;
import com.uade.tpo.wepadel.persistencia.repository.PedidoRepository;
import com.uade.tpo.wepadel.persistencia.repository.ProductoRepository;
import com.uade.tpo.wepadel.persistencia.repository.SistemaPuntosRepository;
import com.uade.tpo.wepadel.persistencia.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Carga datos demo al primer arranque si la base MySQL esta vacia. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataSeeder implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionSistemaRepository configuracionRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final CarritoRepository carritoRepository;
    private final SistemaPuntosRepository puntosRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        log.info("Base vacia: cargando datos demo de WePadel...");

        ConfiguracionSistema config = new ConfiguracionSistema();
        config.setCostoEnvio(new BigDecimal("1500.00"));
        config.setCanalNotificacionDefault("EMAIL");
        config.setPesosPorPuntoGenerado(new BigDecimal("10.00"));
        config.setConversionPuntos(100);
        configuracionRepository.save(config);

        Categoria paletas = saveCat("Paletas", null);
        Categoria pelotas = saveCat("Pelotas", null);
        Categoria accesorios = saveCat("Accesorios", null);
        Categoria calzado = saveCat("Calzado", null);
        Categoria palControl = saveCat("Control", paletas);
        Categoria palPotencia = saveCat("Potencia", paletas);
        Categoria palHibrida = saveCat("Hibrida", paletas);
        Categoria accBolsos = saveCat("Bolsos", accesorios);
        Categoria accOvergrips = saveCat("Overgrips", accesorios);
        Categoria accProtectores = saveCat("Protectores", accesorios);
        Categoria calHombre = saveCat("Hombre", calzado);
        Categoria calMujer = saveCat("Mujer", calzado);

        Usuario admin = saveUsuario("Admin Sistema", "admin@wepadel.com", "admin123", RolEnum.ADMINISTRADOR, null);
        Usuario cliente = saveUsuario("Juan Cliente", "cliente@wepadel.com", "cliente123", RolEnum.CLIENTE,
                CanalNotificacionEnum.EMAIL);

        Carrito carrito = new Carrito(cliente);
        carritoRepository.save(carrito);

        SistemaPuntos puntos = new SistemaPuntos(cliente, 100);
        puntos.setCantidad(500);
        puntosRepository.save(puntos);

        Paleta paletaCarrito = savePaleta("Bullpadel Vertex 04 Comfort",
                "Paleta de control, balance medio, fibra de carbono", "89900", 15, palControl,
                365, "Medio", "Redonda", "Fibra de carbono");
        savePaleta("Nox AT10 Genius 18K", "Paleta de control para jugadores avanzados", "75000", 8, palControl,
                355, "Bajo", "Lagrimas", "Carbono 18K");
        savePaleta("Siux Trilogy 3", "Paleta de potencia, salida explosiva", "105000", 6, palPotencia,
                370, "Alto", "Diamante", "Fibra de vidrio y carbono");
        savePaleta("Head Delta Pro", "Paleta hibrida versatil para todos los niveles", "92000", 10, palHibrida,
                360, "Medio-Alto", "Hibrida", "Graphene");

        Pelota pelotaPedido = savePelota("Head Pro S", "Pelotas oficiales de competicion, tubo x3", "12000", 50,
                pelotas, "Alta", 3);
        savePelota("Bullpadel Premium", "Pelotas de entrenamiento, tubo x4", "9500", 80, pelotas, "Media", 4);

        saveAccesorio("Overgrip Wilson Pro", "Pack x3 overgrips antideslizantes", "4500", 120, accOvergrips,
                "Overgrip", "Poliuretano");
        saveAccesorio("Protector Bullpadel", "Protector de marco transparente", "8900", 60, accProtectores,
                "Protector", "Silicona");
        saveAccesorio("Mochila Nox Pro", "Mochila padel con compartimento termico", "45999", 25, accBolsos,
                "Mochila", "Nylon");
        saveAccesorio("Bolso Bullpadel Tour", "Bolso racket bag 3 paletas", "38999", 18, accBolsos,
                "Bolso", "Poliester");

        saveCalzado("Bullpadel Hybrid Fly 24", "Zapatilla de padel amortiguacion alta", "119999", 20, calHombre,
                42, "Blanco/Azul", "Hombre");
        saveCalzado("Adidas Courtjam Control", "Zapatilla indoor padel", "99999", 18, calHombre,
                40, "Negro", "Hombre");
        saveCalzado("Asics Gel Padel Pro", "Zapatilla mujer padel", "109999", 15, calMujer,
                38, "Rosa", "Mujer");

        carrito.getItems().add(new CarritoItem(carrito, paletaCarrito, 1));
        carritoRepository.save(carrito);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setTotal(new BigDecimal("13500.00"));
        pedido.setCostoEnvio(new BigDecimal("1500.00"));
        pedido.setDescuentoPorPuntos(BigDecimal.ZERO);
        pedido.setFechaCompra(LocalDateTime.now().minusDays(7));
        pedido.setEstado(EstadoPedidoEnum.ENTREGADO);
        pedido.setTipoPago(TipoPagoEnum.TARJETA);
        pedido.setCodigoTransaccion("TX-DEMO-001");
        pedido.setUsaPuntos(false);
        pedido.setPuntosUsados(0);
        pedido.setPuntosGenerados(120);
        pedido.getItems().add(new ItemPedido(pedido, pelotaPedido, 1,
                new BigDecimal("12000.00"), "Head Pro S"));
        pedidoRepository.save(pedido);

        log.info("Datos demo listos. Admin: admin@wepadel.com / admin123 | Cliente: cliente@wepadel.com / cliente123");
    }

    private Categoria saveCat(String nombre, Categoria parent) {
        return categoriaRepository.save(new Categoria(nombre, parent));
    }

    private Usuario saveUsuario(String nombre, String mail, String password, RolEnum rol,
                                CanalNotificacionEnum canal) {
        Usuario u = new Usuario();
        u.setNombreApellido(nombre);
        u.setMail(mail);
        u.setPassword(password);
        u.setRol(rol);
        u.setCanalNotificacion(canal);
        u.setFechaCreacion(LocalDateTime.now());
        return usuarioRepository.save(u);
    }

    private Paleta savePaleta(String nombre, String desc, String precio, int stock, Categoria cat,
                              int peso, String balance, String forma, String material) {
        return productoRepository.save(new Paleta(nombre, desc, new BigDecimal(precio), stock, cat,
                peso, balance, forma, material));
    }

    private Pelota savePelota(String nombre, String desc, String precio, int stock, Categoria cat,
                              String presion, int unidades) {
        return (Pelota) productoRepository.save(new Pelota(nombre, desc, new BigDecimal(precio), stock, cat,
                presion, unidades));
    }

    private Accesorio saveAccesorio(String nombre, String desc, String precio, int stock, Categoria cat,
                                    String tipo, String material) {
        return (Accesorio) productoRepository.save(new Accesorio(nombre, desc, new BigDecimal(precio), stock, cat,
                tipo, material));
    }

    private Calzado saveCalzado(String nombre, String desc, String precio, int stock, Categoria cat,
                                int talle, String color, String genero) {
        return (Calzado) productoRepository.save(new Calzado(nombre, desc, new BigDecimal(precio), stock, cat,
                talle, color, genero));
    }
}
