package com.floratta.model.dto;

public class LoginDTO {

    private String correo;
    private String password;

    public LoginDTO() {}

    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
}