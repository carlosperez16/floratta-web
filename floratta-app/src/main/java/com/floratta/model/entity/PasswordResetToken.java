package com.floratta.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private Boolean usado = false;

    public PasswordResetToken() {}

    public Long getId() { return id; }
    public String getCorreo() { return correo; }
    public String getToken() { return token; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public Boolean getUsado() { return usado; }

    public void setId(Long id) { this.id = id; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setToken(String token) { this.token = token; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public void setUsado(Boolean usado) { this.usado = usado; }
}