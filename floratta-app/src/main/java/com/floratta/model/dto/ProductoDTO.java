package com.floratta.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    private String imagenUrl;
    private Boolean disponible;
    private Long categoriaId;

    public ProductoDTO() {}

    public ProductoDTO(Long id, String nombre, String descripcion,
                       BigDecimal precio, String imagenUrl,
                       Boolean disponible, Long categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.disponible = disponible;
        this.categoriaId = categoriaId;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }
    public Boolean getDisponible() { return disponible; }
    public Long getCategoriaId() { return categoriaId; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}