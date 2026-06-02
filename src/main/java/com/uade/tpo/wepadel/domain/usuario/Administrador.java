package com.uade.tpo.wepadel.domain.usuario;

import com.uade.tpo.wepadel.domain.catalogo.Accesorio;
import com.uade.tpo.wepadel.domain.catalogo.Calzado;
import com.uade.tpo.wepadel.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.domain.catalogo.DatosAccesorio;
import com.uade.tpo.wepadel.domain.catalogo.DatosCalzado;
import com.uade.tpo.wepadel.domain.catalogo.DatosPaleta;
import com.uade.tpo.wepadel.domain.catalogo.DatosPelota;
import com.uade.tpo.wepadel.domain.catalogo.Paleta;
import com.uade.tpo.wepadel.domain.catalogo.Pelota;
import com.uade.tpo.wepadel.domain.catalogo.Producto;
import com.uade.tpo.wepadel.domain.pedido.Pedido;
import com.uade.tpo.wepadel.domain.pedido.estado.EstadoPedido;
import com.uade.tpo.wepadel.domain.pedido.estado.EstadoPedidoFactory;

import java.util.function.LongSupplier;

public class Administrador extends Usuario {

    private LongSupplier generadorIdProducto;

    public Administrador(Long id, String nombreApellido, String mail, String password) {
        super(id, nombreApellido, mail, password);
    }

    public void setGeneradorIdProducto(LongSupplier generadorIdProducto) {
        this.generadorIdProducto = generadorIdProducto;
    }

    @Override
    public RolEnum getRol() {
        return RolEnum.ADMINISTRADOR;
    }

    public Categoria crearCategoria(String nombre, Categoria padre) {
        Categoria categoria = new Categoria(nombre);
        if (padre != null) {
            padre.agregar(categoria);
        }
        return categoria;
    }

    public Paleta crearPaleta(DatosPaleta datos) {
        validarGeneradorProducto();
        Paleta paleta = new Paleta(
                generadorIdProducto.getAsLong(),
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                true,
                datos.categoria(),
                datos.pesoGramos(),
                datos.balance(),
                datos.forma(),
                datos.material()
        );
        datos.categoria().agregar(paleta);
        return paleta;
    }

    public Pelota crearPelota(DatosPelota datos) {
        validarGeneradorProducto();
        Pelota pelota = new Pelota(
                generadorIdProducto.getAsLong(),
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                true,
                datos.categoria(),
                datos.presion(),
                datos.unidadesPorTubo()
        );
        datos.categoria().agregar(pelota);
        return pelota;
    }

    public Accesorio crearAccesorio(DatosAccesorio datos) {
        validarGeneradorProducto();
        Accesorio accesorio = new Accesorio(
                generadorIdProducto.getAsLong(),
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                true,
                datos.categoria(),
                datos.tipo(),
                datos.material()
        );
        datos.categoria().agregar(accesorio);
        return accesorio;
    }

    public Calzado crearCalzado(DatosCalzado datos) {
        validarGeneradorProducto();
        Calzado calzado = new Calzado(
                generadorIdProducto.getAsLong(),
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                true,
                datos.categoria(),
                datos.talle(),
                datos.color(),
                datos.genero()
        );
        datos.categoria().agregar(calzado);
        return calzado;
    }

    public void actualizarStock(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        producto.setStock(cantidad);
    }

    public void cambiarEstadoPedido(Pedido pedido, String nuevoEstado) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        EstadoPedido estado = EstadoPedidoFactory.fromNombre(nuevoEstado);
        switch (estado.getNombre()) {
            case "PAGADO" -> pedido.marcarPagado();
            case "ENVIADO" -> pedido.enviar();
            case "ENTREGADO" -> pedido.entregar();
            case "CANCELADO" -> pedido.cancelar();
            default -> throw new IllegalArgumentException("Transicion no soportada desde facade: " + nuevoEstado);
        }
    }

    private void validarGeneradorProducto() {
        if (generadorIdProducto == null) {
            throw new IllegalStateException("Generador de ID de producto no configurado");
        }
    }
}
