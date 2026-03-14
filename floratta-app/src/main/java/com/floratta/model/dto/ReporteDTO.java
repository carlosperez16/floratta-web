package com.floratta.model.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReporteDTO {

    private BigDecimal totalVentas;
    private long totalPedidos;
    private Map<String, Long> pedidosPorEstado;
    private List<ItemVendidoDTO> productosMasVendidos;
    private List<ItemVendidoDTO> cursosMasVendidos;

    public static class ItemVendidoDTO {
        private String nombre;
        private long cantidad;
        private BigDecimal totalGenerado;

        public ItemVendidoDTO(String nombre, long cantidad, BigDecimal totalGenerado) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.totalGenerado = totalGenerado;
        }

        public String getNombre() { return nombre; }
        public long getCantidad() { return cantidad; }
        public BigDecimal getTotalGenerado() { return totalGenerado; }
    }

    public BigDecimal getTotalVentas() { return totalVentas; }
    public void setTotalVentas(BigDecimal totalVentas) { this.totalVentas = totalVentas; }
    public long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }
    public Map<String, Long> getPedidosPorEstado() { return pedidosPorEstado; }
    public void setPedidosPorEstado(Map<String, Long> pedidosPorEstado) { this.pedidosPorEstado = pedidosPorEstado; }
    public List<ItemVendidoDTO> getProductosMasVendidos() { return productosMasVendidos; }
    public void setProductosMasVendidos(List<ItemVendidoDTO> productosMasVendidos) { this.productosMasVendidos = productosMasVendidos; }
    public List<ItemVendidoDTO> getCursosMasVendidos() { return cursosMasVendidos; }
    public void setCursosMasVendidos(List<ItemVendidoDTO> cursosMasVendidos) { this.cursosMasVendidos = cursosMasVendidos; }
}