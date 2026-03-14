package com.floratta.service;

import com.floratta.model.entity.Pedido;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarConfirmacionPedido(String destinatario, Pedido pedido) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("florattapasteleria@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject("¡Tu pedido #" + pedido.getId() + " fue confirmado! 🎂");

            String htmlContent = "<div style='font-family: Arial; color: #333;'>"
                + "<h2 style='color: #6B4F3B;'>¡Hola!</h2>"
                + "<p>Tu pedido <strong>#" + pedido.getId() + "</strong> ha sido confirmado.</p>"
                + "<p><strong>Total:</strong> $" + pedido.getTotal() + "</p>"
                + "<p><strong>Fecha de entrega:</strong> " + pedido.getFechaEntrega() + "</p>"
                + "<p>¡Gracias por elegir Floratta Pastelería! 🌸</p>"
                + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error enviando email: " + e.getMessage());
        }
    }

    @Async
    public void enviarCambioEstado(String destinatario, Long pedidoId, String estado) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("florattapasteleria@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject("Actualización de tu pedido #" + pedidoId);
            helper.setText("<p>Tu pedido <strong>#" + pedidoId
                + "</strong> cambió a estado: <strong>" + estado + "</strong></p>", true);

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error enviando email: " + e.getMessage());
        }
    }
    
    @Async
public void enviarEmailRecuperacion(String destinatario, String token) {
    try {
        String enlace = "http://localhost:8080/recuperar-password/nueva?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("floratta.bakestudio@gmail.com");
        helper.setTo(destinatario);
        helper.setSubject("Floratta Bake Studio — Recupera tu contraseña 🌸");

        String html = "<div style='font-family:Arial; color:#333; max-width:520px; margin:auto;'>"
            + "<div style='background:#2d4a3e; padding:28px; text-align:center;'>"
            + "<h1 style='color:#c8c9a8; margin:0; letter-spacing:2px;'>Floratta Bake Studio</h1>"
            + "</div>"
            + "<div style='padding:32px;'>"
            + "<p style='color:#2d4a3e; font-size:16px;'>Recibimos una solicitud para restablecer tu contraseña.</p>"
            + "<p style='color:#555;'>Este enlace es válido por <strong>30 minutos</strong>.</p>"
            + "<div style='text-align:center; margin:32px 0;'>"
            + "<a href='" + enlace + "' style='background:#2d4a3e; color:#c8c9a8; padding:14px 36px;"
            + "border-radius:8px; text-decoration:none; font-size:15px;'>Restablecer contraseña</a>"
            + "</div>"
            + "<p style='color:#aaa; font-size:13px; text-align:center;'>"
            + "Si no solicitaste esto, ignora este correo.</p>"
            + "</div>"
            + "<div style='background:#c8c9a8; padding:14px; text-align:center;'>"
            + "<p style='color:#2d4a3e; font-size:12px; margin:0;'>"
            + "© 2025 Floratta Bake Studio</p>"
            + "</div></div>";

        helper.setText(html, true);
        mailSender.send(message);

    } catch (MessagingException e) {
        System.err.println("Error enviando email de recuperación: " + e.getMessage());
    }
}
}