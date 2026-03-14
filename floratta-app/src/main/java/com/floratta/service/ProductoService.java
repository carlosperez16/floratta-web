package com.floratta.service;

import com.floratta.model.dto.ProductoDTO;
import com.floratta.model.entity.Categoria;
import com.floratta.model.entity.Producto;
import com.floratta.repository.CategoriaRepository;
import com.floratta.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findByDisponibleTrue();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Transactional
    public Producto crearProducto(ProductoDTO dto) {
        if (productoRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + dto.getNombre());
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : true);

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + dto.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Long id, ProductoDTO dto) {
        Producto producto = obtenerPorId(id);

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        if (dto.getImagenUrl() != null) producto.setImagenUrl(dto.getImagenUrl());
        if (dto.getDisponible() != null) producto.setDisponible(dto.getDisponible());

        return productoRepository.save(producto);
    }

    @Transactional
    public void desactivarProducto(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setDisponible(false);
        productoRepository.save(producto);
    }
}