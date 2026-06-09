package com.uade.tpo.wepadel.persistencia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.persistencia.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional
    public void sincronizarStock(Producto productoDomain) {
        com.uade.tpo.wepadel.persistencia.entity.Producto productoEntity = productoRepository.findById(productoDomain.getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoDomain.getId()));
        productoEntity.setStock(productoDomain.getStock());
        productoRepository.save(productoEntity);
    }

    @Transactional(readOnly = true)
    public long siguienteIdProducto() {
        return productoRepository.findAll().stream()
                .mapToLong(com.uade.tpo.wepadel.persistencia.entity.Producto::getId)
                .max()
                .orElse(0L) + 1L;
    }
}
