package com.floratta.controller;

import com.floratta.model.entity.Pedido;
import com.floratta.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PedidoService pedidoService;


    @GetMapping("/pedidos")
    public List<Pedido> obtenerPedidosAdmin() {
        return pedidoService.obtenerTodos();
    }

}