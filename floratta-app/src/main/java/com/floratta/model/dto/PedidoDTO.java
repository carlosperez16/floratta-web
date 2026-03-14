package com.floratta.model.dto;

import java.util.List;

public class PedidoDTO {

    private String nombreCliente;
    private String telefono;
    private String direccion;
    private String notas;
    private List<ItemDTO> items;

    public static class ItemDTO {
        private Long productoId;   
        private Long cursoId;      
        private int cantidad;

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Long getCursoId() { return cursoId; }
        public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
}