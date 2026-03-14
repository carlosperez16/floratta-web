package com.floratta.repository;

import com.floratta.model.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteRepository extends JpaRepository<DetallePedido, Long> {

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p " +
           "WHERE p.fechaCreacion BETWEEN :desde AND :hasta " +
           "AND p.estado != 'CANCELADO'")
    BigDecimal totalVentasEnRango(@Param("desde") LocalDateTime desde,
                                  @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COUNT(p) FROM Pedido p " +
           "WHERE p.fechaCreacion BETWEEN :desde AND :hasta")
    long totalPedidosEnRango(@Param("desde") LocalDateTime desde,
                              @Param("hasta") LocalDateTime hasta);

    @Query("SELECT p.estado, COUNT(p) FROM Pedido p " +
           "WHERE p.fechaCreacion BETWEEN :desde AND :hasta " +
           "GROUP BY p.estado")
    List<Object[]> pedidosPorEstadoEnRango(@Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta);

    @Query("SELECT d.producto.nombre, SUM(d.cantidad), SUM(d.precioUnitario * d.cantidad) " +
           "FROM DetallePedido d " +
           "WHERE d.producto IS NOT NULL " +
           "AND d.pedido.fechaCreacion BETWEEN :desde AND :hasta " +
           "AND d.pedido.estado != 'CANCELADO' " +
           "GROUP BY d.producto.nombre " +
           "ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> productosMasVendidosEnRango(@Param("desde") LocalDateTime desde,
                                                @Param("hasta") LocalDateTime hasta);

    @Query("SELECT d.curso.nombre, SUM(d.cantidad), SUM(d.precioUnitario * d.cantidad) " +
           "FROM DetallePedido d " +
           "WHERE d.curso IS NOT NULL " +
           "AND d.pedido.fechaCreacion BETWEEN :desde AND :hasta " +
           "AND d.pedido.estado != 'CANCELADO' " +
           "GROUP BY d.curso.nombre " +
           "ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> cursosMasVendidosEnRango(@Param("desde") LocalDateTime desde,
                                             @Param("hasta") LocalDateTime hasta);
}