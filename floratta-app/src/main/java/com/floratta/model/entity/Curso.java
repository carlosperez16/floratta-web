package com.floratta.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Lob
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 100)
    private String duracion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModalidadCurso modalidad;

    @Column
    private Integer cupos;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(nullable = false)
    private Boolean disponible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaCurso categoria;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.disponible == null) this.disponible = true;

        if (this.modalidad == ModalidadCurso.PRESENCIAL) {
            if (this.cupos == null || this.cupos > 15) {
                this.cupos = 15;
            }
            if (this.cupos < 1) this.cupos = 1;
        } else if (this.modalidad == ModalidadCurso.VIRTUAL) {
            this.cupos = null;
        }
    }

    public Curso() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public String getDuracion() { return duracion; }
    public ModalidadCurso getModalidad() { return modalidad; }
    public Integer getCupos() { return cupos; }
    public String getImagenUrl() { return imagenUrl; }
    public Boolean getDisponible() { return disponible; }
    public CategoriaCurso getCategoria() { return categoria; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public void setModalidad(ModalidadCurso modalidad) { this.modalidad = modalidad; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public void setCategoria(CategoriaCurso categoria) { this.categoria = categoria; }
}