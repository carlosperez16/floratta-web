package com.floratta.service;

import com.floratta.model.dto.ReporteDTO;
import com.floratta.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public ReporteDTO generarReporte(LocalDateTime desde, LocalDateTime hasta) {
        ReporteDTO reporte = new ReporteDTO();

        BigDecimal total = reporteRepository.totalVentasEnRango(desde, hasta);
        reporte.setTotalVentas(total != null ? total : BigDecimal.ZERO);

        reporte.setTotalPedidos(reporteRepository.totalPedidosEnRango(desde, hasta));

        List<Object[]> porEstado = reporteRepository.pedidosPorEstadoEnRango(desde, hasta);
        Map<String, Long> estadoMap = new LinkedHashMap<>();
        for (Object[] row : porEstado) {
            estadoMap.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        reporte.setPedidosPorEstado(estadoMap);

        List<Object[]> productos = reporteRepository.productosMasVendidosEnRango(desde, hasta);
        reporte.setProductosMasVendidos(productos.stream()
            .map(row -> new ReporteDTO.ItemVendidoDTO(
                (String) row[0],
                ((Number) row[1]).longValue(),
                (BigDecimal) row[2]
            ))
            .collect(Collectors.toList()));

        List<Object[]> cursos = reporteRepository.cursosMasVendidosEnRango(desde, hasta);
        reporte.setCursosMasVendidos(cursos.stream()
            .map(row -> new ReporteDTO.ItemVendidoDTO(
                (String) row[0],
                ((Number) row[1]).longValue(),
                (BigDecimal) row[2]
            ))
            .collect(Collectors.toList()));

        return reporte;
    }
}