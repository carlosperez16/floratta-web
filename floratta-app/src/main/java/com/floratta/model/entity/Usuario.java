package com.floratta.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150, unique = true)
    private String correo;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol = RolUsuario.CLIENTE;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.rol == null) this.rol = RolUsuario.CLIENTE;
    }

    public Usuario() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTelefono() { return telefono; }
    public RolUsuario getRol() { return rol; }
    public Boolean getActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public List<Pedido> getPedidos() { return pedidos; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRol(RolUsuario rol) { this.rol = rol; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
}