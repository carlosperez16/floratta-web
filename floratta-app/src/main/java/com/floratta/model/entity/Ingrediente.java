package com.floratta.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "unidad_medida", length = 30)
    private String unidadMedida;

    @Column(name = "stock_actual", precision = 10, scale = 3)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(name = "stock_minimo", precision = 10, scale = 3)
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.stockActual == null) this.stockActual = BigDecimal.ZERO;
        if (this.stockMinimo == null) this.stockMinimo = BigDecimal.ZERO;
    }

    public Ingrediente() {}

    public Ingrediente(Long id, String nombre, String unidadMedida,
                       BigDecimal stockActual, BigDecimal stockMinimo, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUnidadMedida() { return unidadMedida; }
    public BigDecimal getStockActual() { return stockActual; }
    public BigDecimal getStockMinimo() { return stockMinimo; }
    public Boolean getActivo() { return activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public void setStockActual(BigDecimal stockActual) { this.stockActual = stockActual; }
    public void setStockMinimo(BigDecimal stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}