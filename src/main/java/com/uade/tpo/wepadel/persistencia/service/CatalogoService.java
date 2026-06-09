package com.uade.tpo.wepadel.persistencia.service;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.backend.domain.catalogo.Categoria;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosAccesorio;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosCalzado;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosPaleta;
import com.uade.tpo.wepadel.backend.domain.catalogo.DatosPelota;
import com.uade.tpo.wepadel.backend.domain.catalogo.Producto;
import com.uade.tpo.wepadel.persistencia.mapper.DomainMapper;
import com.uade.tpo.wepadel.persistencia.repository.CategoriaRepository;
import com.uade.tpo.wepadel.persistencia.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final DomainMapper domainMapper;

    private final IdentityHashMap<Categoria, Long> categoriaIds = new IdentityHashMap<>();
    private final Map<Long, Producto> productosPorId = new HashMap<>();
    private Categoria catalogoRaiz;

    @Transactional(readOnly = true)
    public Categoria construirCatalogo() {
        categoriaIds.clear();
        productosPorId.clear();

        catalogoRaiz = new Categoria("WePadel");
        categoriaRepository.findByParentIsNull().ifPresent(raizEntity -> {
            catalogoRaiz = new Categoria(raizEntity.getNombre());
            categoriaIds.put(catalogoRaiz, raizEntity.getId());
            construirHijos(raizEntity.getId(), catalogoRaiz);
        });

        for (com.uade.tpo.wepadel.persistencia.entity.Producto productoEntity : productoRepository.findAll()) {
            Categoria categoriaDomain = buscarCategoriaDomain(productoEntity.getCategoria().getId());
            Producto productoDomain = domainMapper.toDomainProducto(productoEntity, categoriaDomain);
            categoriaDomain.agregar(productoDomain);
            productosPorId.put(productoEntity.getId(), productoDomain);
        }

        return catalogoRaiz;
    }

    private void construirHijos(Long parentEntityId, Categoria parentDomain) {
        for (com.uade.tpo.wepadel.persistencia.entity.Categoria hijoEntity : categoriaRepository.findByParentId(parentEntityId)) {
            Categoria hijoDomain = new Categoria(hijoEntity.getNombre());
            parentDomain.agregar(hijoDomain);
            categoriaIds.put(hijoDomain, hijoEntity.getId());
            construirHijos(hijoEntity.getId(), hijoDomain);
        }
    }

    private Categoria buscarCategoriaDomain(Long categoriaEntityId) {
        return categoriaIds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(categoriaEntityId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Categoria no encontrada en arbol: " + categoriaEntityId));
    }

    @Transactional
    public Categoria crearCategoria(String nombre, Categoria padreDomain) {
        Long parentId = categoriaIds.get(padreDomain);
        com.uade.tpo.wepadel.persistencia.entity.Categoria parentEntity = parentId == null
                ? null
                : categoriaRepository.getReferenceById(parentId);

        com.uade.tpo.wepadel.persistencia.entity.Categoria categoriaEntity =
                categoriaRepository.save(new com.uade.tpo.wepadel.persistencia.entity.Categoria(nombre, parentEntity));

        Categoria categoriaDomain = new Categoria(nombre);
        padreDomain.agregar(categoriaDomain);
        categoriaIds.put(categoriaDomain, categoriaEntity.getId());
        return categoriaDomain;
    }

    @Transactional
    public Producto guardarPaleta(DatosPaleta datos) {
        Long categoriaId = categoriaIds.get(datos.categoria());
        com.uade.tpo.wepadel.persistencia.entity.Categoria categoriaEntity = categoriaRepository.getReferenceById(categoriaId);
        com.uade.tpo.wepadel.persistencia.entity.Paleta paletaEntity = new com.uade.tpo.wepadel.persistencia.entity.Paleta(
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                categoriaEntity,
                datos.pesoGramos(),
                datos.balance(),
                datos.forma(),
                datos.material()
        );
        paletaEntity = (com.uade.tpo.wepadel.persistencia.entity.Paleta) productoRepository.save(paletaEntity);
        Producto productoDomain = domainMapper.toDomainProducto(paletaEntity, datos.categoria());
        datos.categoria().agregar(productoDomain);
        productosPorId.put(paletaEntity.getId(), productoDomain);
        return productoDomain;
    }

    @Transactional
    public Producto guardarPelota(DatosPelota datos) {
        Long categoriaId = categoriaIds.get(datos.categoria());
        com.uade.tpo.wepadel.persistencia.entity.Categoria categoriaEntity = categoriaRepository.getReferenceById(categoriaId);
        com.uade.tpo.wepadel.persistencia.entity.Pelota pelotaEntity = new com.uade.tpo.wepadel.persistencia.entity.Pelota(
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                categoriaEntity,
                datos.presion(),
                datos.unidadesPorTubo()
        );
        pelotaEntity = (com.uade.tpo.wepadel.persistencia.entity.Pelota) productoRepository.save(pelotaEntity);
        Producto productoDomain = domainMapper.toDomainProducto(pelotaEntity, datos.categoria());
        datos.categoria().agregar(productoDomain);
        productosPorId.put(pelotaEntity.getId(), productoDomain);
        return productoDomain;
    }

    @Transactional
    public Producto guardarAccesorio(DatosAccesorio datos) {
        Long categoriaId = categoriaIds.get(datos.categoria());
        com.uade.tpo.wepadel.persistencia.entity.Categoria categoriaEntity = categoriaRepository.getReferenceById(categoriaId);
        com.uade.tpo.wepadel.persistencia.entity.Accesorio accesorioEntity = new com.uade.tpo.wepadel.persistencia.entity.Accesorio(
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                categoriaEntity,
                datos.tipo(),
                datos.material()
        );
        accesorioEntity = (com.uade.tpo.wepadel.persistencia.entity.Accesorio) productoRepository.save(accesorioEntity);
        Producto productoDomain = domainMapper.toDomainProducto(accesorioEntity, datos.categoria());
        datos.categoria().agregar(productoDomain);
        productosPorId.put(accesorioEntity.getId(), productoDomain);
        return productoDomain;
    }

    @Transactional
    public Producto guardarCalzado(DatosCalzado datos) {
        Long categoriaId = categoriaIds.get(datos.categoria());
        com.uade.tpo.wepadel.persistencia.entity.Categoria categoriaEntity = categoriaRepository.getReferenceById(categoriaId);
        com.uade.tpo.wepadel.persistencia.entity.Calzado calzadoEntity = new com.uade.tpo.wepadel.persistencia.entity.Calzado(
                datos.nombre(),
                datos.descripcion(),
                datos.precio(),
                datos.stock(),
                categoriaEntity,
                datos.talle(),
                datos.color(),
                datos.genero()
        );
        calzadoEntity = (com.uade.tpo.wepadel.persistencia.entity.Calzado) productoRepository.save(calzadoEntity);
        Producto productoDomain = domainMapper.toDomainProducto(calzadoEntity, datos.categoria());
        datos.categoria().agregar(productoDomain);
        productosPorId.put(calzadoEntity.getId(), productoDomain);
        return productoDomain;
    }

    public List<Producto> buscarProductos(String filtro) {
        if (catalogoRaiz == null) {
            construirCatalogo();
        }
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

    public Map<Long, Producto> getProductosPorId() {
        if (productosPorId.isEmpty()) {
            construirCatalogo();
        }
        return productosPorId;
    }

    public Categoria getCatalogoRaiz() {
        if (catalogoRaiz == null) {
            construirCatalogo();
        }
        return catalogoRaiz;
    }
}
