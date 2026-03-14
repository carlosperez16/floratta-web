package com.floratta.controller;

import com.floratta.model.dto.LoginDTO;
import com.floratta.model.dto.RegistroDTO;
import com.floratta.model.entity.Usuario;
import com.floratta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroDTO dto) {
        try {
            Usuario usuario = usuarioService.registrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "mensaje", "Usuario registrado exitosamente",
                    "id", usuario.getId(),
                    "correo", usuario.getCorreo()
                ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            String token = usuarioService.login(dto);
            Usuario usuario = usuarioService.buscarPorCorreo(dto.getCorreo());
            return ResponseEntity.ok(Map.of(
                "token", token,
                "tipo", "Bearer",
                "mensaje", "Login exitoso",
                "rol", usuario.getRol().name()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
    }
}
}