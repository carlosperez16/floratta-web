package com.floratta.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorias_cursos")
public class CategoriaCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80, unique = true)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curso> cursos;

    public CategoriaCurso() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public List<Curso> getCursos() { return cursos; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCursos(List<Curso> cursos) { this.cursos = cursos; }
}