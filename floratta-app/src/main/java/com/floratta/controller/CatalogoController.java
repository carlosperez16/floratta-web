package com.floratta.controller;

import com.floratta.repository.CategoriaRepository;
import com.floratta.repository.ProductoRepository;
import com.floratta.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("productos", productoService.obtenerProductosDisponibles());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "index";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String categoria, Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("categoriaActiva", categoria);

        if (categoria != null && !categoria.isBlank()) {
            categoriaRepository.findByNombreIgnoreCase(categoria).ifPresentOrElse(
                cat -> model.addAttribute("productos",
                    productoRepository.findByCategoriaId(cat.getId())),
                () -> model.addAttribute("productos",
                    productoService.obtenerProductosDisponibles())
            );
        } else {
            model.addAttribute("productos", productoService.obtenerProductosDisponibles());
        }

        return "catalogo";
    }
    
    @GetMapping("/galeria")
    public String galeria(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "galeria";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/pago")
    public String pago() {
        return "pago";
    }

}