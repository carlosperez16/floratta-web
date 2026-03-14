package com.floratta.controller;

import com.floratta.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recuperar-password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping
    public String mostrarFormularioSolicitud() {
        return "recuperar-password";
    }

    @PostMapping
    public String procesarSolicitud(@RequestParam String correo,
                                    RedirectAttributes redirectAttributes) {
        passwordResetService.solicitarReset(correo);

        redirectAttributes.addFlashAttribute("mensaje",
            "Si el correo está registrado, recibirás un enlace en los próximos minutos.");
        return "redirect:/recuperar-password";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaPassword(@RequestParam String token, Model model) {
        boolean tokenValido = passwordResetService.validarToken(token);

        if (!tokenValido) {
            model.addAttribute("error", "El enlace no es válido o ha expirado.");
            return "recuperar-password-expirado";
        }

        model.addAttribute("token", token);
        return "nueva-password";
    }

    @PostMapping("/nueva")
    public String procesarNuevaPassword(@RequestParam String token,
                                         @RequestParam String password,
                                         @RequestParam String confirmarPassword,
                                         RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/recuperar-password/nueva?token=" + token;
        }

        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error",
                "La contraseña debe tener al menos 6 caracteres.");
            return "redirect:/recuperar-password/nueva?token=" + token;
        }

        boolean exito = passwordResetService.resetearPassword(token, password);

        if (exito) {
            redirectAttributes.addFlashAttribute("mensaje",
                "¡Contraseña actualizada! Ya puedes iniciar sesión.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error",
                "El enlace no es válido o ha expirado.");
            return "redirect:/recuperar-password";
        }
    }
}