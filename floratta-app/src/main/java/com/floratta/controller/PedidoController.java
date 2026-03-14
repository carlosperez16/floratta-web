package com.floratta.controller;

import com.floratta.model.dto.PedidoDTO;
import com.floratta.model.entity.EstadoPedido;
import com.floratta.model.entity.Pedido;
import com.floratta.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO dto, Authentication auth) {
        try {
            String correo = auth.getName();
            Pedido pedido = pedidoService.crearPedido(dto, correo);
            return ResponseEntity.ok(Map.of(
                "id",      pedido.getId(),
                "total",   pedido.getTotal(),
                "estado",  pedido.getEstado(),
                "mensaje", "Pedido registrado exitosamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/admin/todos")
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @PutMapping("/admin/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            EstadoPedido estado = EstadoPedido.valueOf(body.get("estado"));
            Pedido pedido = pedidoService.cambiarEstado(id, estado);
            return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado", "estado", pedido.getEstado()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}