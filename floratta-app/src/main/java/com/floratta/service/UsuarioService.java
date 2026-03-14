package com.floratta.service;

import com.floratta.model.dto.LoginDTO;
import com.floratta.model.dto.RegistroDTO;
import com.floratta.model.entity.RolUsuario;
import com.floratta.model.entity.Usuario;
import com.floratta.repository.UsuarioRepository;
import com.floratta.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario registrar(RegistroDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado: " + dto.getCorreo());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(RolUsuario.CLIENTE);

        return usuarioRepository.save(usuario);
    }

    public String login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
            .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario desactivado");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol().name());
    }
    
    public Usuario buscarPorCorreo(String correo) {
    return usuarioRepository.findByCorreo(correo)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
}
}