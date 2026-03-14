package com.floratta.model.dto;

public class DetallePedidoDTO {

    private Long productoId;
    private Integer cantidad;
    private String personalizacion;

    public DetallePedidoDTO() {}

    public Long getProductoId() { return productoId; }
    public Integer getCantidad() { return cantidad; }
    public String getPersonalizacion() { return personalizacion; }

    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setPersonalizacion(String personalizacion) { this.personalizacion = personalizacion; }
}