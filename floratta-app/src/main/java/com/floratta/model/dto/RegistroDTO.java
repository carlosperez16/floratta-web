package com.floratta.model.dto;

public class RegistroDTO {

    private String nombre;
    private String correo;
    private String password;
    private String telefono;

    public RegistroDTO() {}

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTelefono() { return telefono; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}