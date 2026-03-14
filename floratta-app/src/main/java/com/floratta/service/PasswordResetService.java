package com.floratta.service;

import com.floratta.model.entity.PasswordResetToken;
import com.floratta.model.entity.Usuario;
import com.floratta.repository.PasswordResetTokenRepository;
import com.floratta.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void solicitarReset(String correo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return;
        }

        Optional<PasswordResetToken> tokenAnterior = tokenRepository.findByCorreo(correo);
        tokenAnterior.ifPresent(tokenRepository::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setCorreo(correo);
        resetToken.setToken(token);
        resetToken.setFechaExpiracion(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsado(false);

        tokenRepository.save(resetToken);

        emailService.enviarEmailRecuperacion(correo, token);
    }

    @Transactional
    public boolean resetearPassword(String token, String nuevaPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.getUsado() || resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return false;
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(resetToken.getCorreo());
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        resetToken.setUsado(true);
        tokenRepository.save(resetToken);

        return true;
    }

    public boolean validarToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();
        return !resetToken.getUsado() && resetToken.getFechaExpiracion().isAfter(LocalDateTime.now());
    }
}