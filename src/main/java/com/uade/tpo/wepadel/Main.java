package com.uade.tpo.wepadel;

import com.uade.tpo.wepadel.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.domain.catalogo.DatosPaleta;
import com.uade.tpo.wepadel.domain.catalogo.DatosPelota;
import com.uade.tpo.wepadel.domain.catalogo.Paleta;
import com.uade.tpo.wepadel.domain.catalogo.Pelota;
import com.uade.tpo.wepadel.domain.catalogo.Producto;
import com.uade.tpo.wepadel.domain.notificacion.NotificadorEmail;
import com.uade.tpo.wepadel.domain.pago.PagoTarjetaCredito;
import com.uade.tpo.wepadel.domain.pedido.Pedido;
import com.uade.tpo.wepadel.domain.usuario.Administrador;
import com.uade.tpo.wepadel.domain.usuario.Cliente;
import com.uade.tpo.wepadel.domain.usuario.RolEnum;
import com.uade.tpo.wepadel.facade.TiendaFacade;

import java.math.BigDecimal;

/**
 * Demo de consola - Fase 1: valida el dominio y patrones sin JPA ni Swing.
 */
public class Main {

    public static void main(String[] args) {
        TiendaFacade tienda = new TiendaFacade();

        System.out.println("=== WePadel PDS - Demo Fase 1 ===\n");

        Administrador admin = (Administrador) tienda.registrarUsuario(
                "Admin WePadel", "admin@wepadel.com", "admin123", RolEnum.ADMINISTRADOR);
        Cliente cliente = (Cliente) tienda.registrarUsuario(
                "Juan Perez", "juan@mail.com", "cliente123", RolEnum.CLIENTE);

        tienda.configurarNotificacion(cliente, new NotificadorEmail(cliente));
        System.out.println("Usuarios registrados: admin + cliente");

        Categoria raiz = tienda.getCatalogoRaiz();
        Categoria paletas = admin.crearCategoria("Paletas", raiz);
        Categoria pelotas = admin.crearCategoria("Pelotas", raiz);

        Paleta paleta = admin.crearPaleta(new DatosPaleta(
                "Bullpadel Vertex 04",
                "Paleta de fibra de vidrio para nivel intermedio",
                new BigDecimal("189999.00"),
                10,
                paletas,
                365,
                "medio",
                "redonda",
                "fibra de vidrio"
        ));

        Pelota pelota = admin.crearPelota(new DatosPelota(
                "Head Pro S",
                "Pelotas oficiales de torneo",
                new BigDecimal("8999.00"),
                50,
                pelotas,
                "11PSI",
                3
        ));

        System.out.println("\nCatalogo creado:");
        System.out.println("  - " + paleta.getNombre() + " | " + paleta.getDescripcionTecnica());
        System.out.println("  - " + pelota.getNombre() + " | " + pelota.getDescripcionTecnica());

        cliente.getSistemaPuntos().sumarPuntos(500);
        System.out.println("\nSaldo inicial de puntos del cliente: " + tienda.consultarSaldoPuntos(cliente));

        tienda.agregarAlCarrito(cliente, paleta, 1);
        tienda.agregarAlCarrito(cliente, pelota, 2);
        System.out.println("Carrito: subtotal $" + cliente.getCarrito().calcularTotal());

        PagoTarjetaCredito pago = new PagoTarjetaCredito(
                "4111111111111111", "Juan Perez", "123", "12/28");

        Pedido pedido = tienda.confirmarCompra(cliente, pago, 200);
        System.out.println("\nCompra confirmada:");
        System.out.println("  Pedido #" + pedido.getId());
        System.out.println("  Total pagado: $" + pedido.getTotal());
        System.out.println("  Descuento por puntos: $" + pedido.getDescuentoPorPuntos());
        System.out.println("  Puntos usados: " + pedido.getPuntosUsados());
        System.out.println("  Puntos generados: " + pedido.getPuntosGenerados());
        System.out.println("  Estado: " + pedido.getEstado().getNombre());
        System.out.println("  Saldo puntos restante: " + tienda.consultarSaldoPuntos(cliente));

        System.out.println("\nAdmin actualiza estado del pedido:");
        tienda.cambiarEstadoPedido(admin, pedido, "ENVIADO");
        tienda.cambiarEstadoPedido(admin, pedido, "ENTREGADO");

        System.out.println("\nBusqueda 'Head':");
        for (Producto p : tienda.buscarProductos("Head")) {
            System.out.println("  - " + p.getNombre());
        }

        System.out.println("\n=== Demo Fase 1 finalizada ===");
    }
}
