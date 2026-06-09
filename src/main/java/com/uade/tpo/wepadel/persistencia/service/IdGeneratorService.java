package com.uade.tpo.wepadel.persistencia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.persistencia.repository.PedidoRepository;
import com.uade.tpo.wepadel.persistencia.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdGeneratorService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public long siguienteIdPedido() {
        return pedidoRepository.findAll().stream()
                .mapToLong(com.uade.tpo.wepadel.persistencia.entity.Pedido::getId)
                .max()
                .orElse(0L) + 1L;
    }

    @Transactional(readOnly = true)
    public long siguienteIdProducto() {
        return productoRepository.findAll().stream()
                .mapToLong(com.uade.tpo.wepadel.persistencia.entity.Producto::getId)
                .max()
                .orElse(0L) + 1L;
    }
}
