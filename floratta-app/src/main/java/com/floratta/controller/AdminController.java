package com.floratta.controller;

import com.floratta.model.entity.Pedido;
import com.floratta.model.entity.Usuario;
import com.floratta.service.PedidoService;
import com.floratta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/pedidos")
    public List<Pedido> obtenerPedidosAdmin() {
        return pedidoService.obtenerTodos();
    }

    @GetMapping("/usuarios")
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerTodos();
    }
}