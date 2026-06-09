package com.uade.tpo.wepadel.persistencia.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.backend.domain.usuario.Cliente;
import com.uade.tpo.wepadel.persistencia.entity.Carrito;
import com.uade.tpo.wepadel.persistencia.entity.CarritoItem;
import com.uade.tpo.wepadel.persistencia.entity.Usuario;
import com.uade.tpo.wepadel.persistencia.repository.CarritoRepository;
import com.uade.tpo.wepadel.persistencia.repository.ProductoRepository;
import com.uade.tpo.wepadel.persistencia.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CatalogoService catalogoService;

    @Transactional
    public Carrito crearCarrito(Usuario usuario) {
        return carritoRepository.save(new Carrito(usuario));
    }

    @Transactional
    public void cargarCarritoEnCliente(Cliente cliente) {
        Carrito carritoEntity = carritoRepository.findByUsuarioId(cliente.getId())
                .orElseGet(() -> crearCarrito(usuarioRepository.getReferenceById(cliente.getId())));

        cliente.getCarrito().vaciar();
        for (CarritoItem item : carritoEntity.getItems()) {
            Producto productoDomain = catalogoService.getProductosPorId().get(item.getProducto().getId());
            if (productoDomain != null) {
                cliente.agregarAlCarrito(productoDomain, item.getCantidad());
            }
        }
    }

    @Transactional
    public void sincronizarCarrito(Cliente cliente) {
        Carrito carritoEntity = carritoRepository.findByUsuarioId(cliente.getId())
                .orElseGet(() -> crearCarrito(usuarioRepository.getReferenceById(cliente.getId())));

        carritoEntity.getItems().clear();
        carritoEntity.setUltimaModificacion(LocalDateTime.now());
        carritoRepository.saveAndFlush(carritoEntity);

        for (com.uade.tpo.wepadel.backend.domain.carrito.ItemCarrito item : cliente.getCarrito().getItems()) {
            com.uade.tpo.wepadel.persistencia.entity.Producto productoEntity =
                    productoRepository.getReferenceById(item.getProducto().getId());
            carritoEntity.getItems().add(new CarritoItem(carritoEntity, productoEntity, item.getCantidad()));
        }
        carritoRepository.save(carritoEntity);
    }

    @Transactional
    public void vaciarCarrito(Cliente cliente) {
        Optional<Carrito> carritoEntity = carritoRepository.findByUsuarioId(cliente.getId());
        carritoEntity.ifPresent(carrito -> {
            carrito.getItems().clear();
            carrito.setUltimaModificacion(LocalDateTime.now());
            carritoRepository.save(carrito);
        });
    }
}
