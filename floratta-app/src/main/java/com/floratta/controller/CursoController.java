package com.floratta.controller;

import com.floratta.model.entity.ModalidadCurso;
import com.floratta.repository.CategoriaCursoRepository;
import com.floratta.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CategoriaCursoRepository categoriaCursoRepository;

    @GetMapping
    public String cursos(@RequestParam(required = false) String categoria,
                         @RequestParam(required = false) String modalidad,
                         Model model) {

        model.addAttribute("categorias", categoriaCursoRepository.findAll());
        model.addAttribute("categoriaActiva", categoria);
        model.addAttribute("modalidadActiva", modalidad);

        if (categoria != null && !categoria.isBlank()) {
            categoriaCursoRepository.findByNombreIgnoreCase(categoria).ifPresentOrElse(
                cat -> model.addAttribute("cursos",
                    cursoService.obtenerPorCategoria(cat.getId())),
                () -> model.addAttribute("cursos",
                    cursoService.obtenerCursosDisponibles())
            );
        } else if (modalidad != null && !modalidad.isBlank()) {
            try {
                ModalidadCurso mod = ModalidadCurso.valueOf(modalidad.toUpperCase());
                model.addAttribute("cursos", cursoService.obtenerPorModalidad(mod));
            } catch (IllegalArgumentException e) {
                model.addAttribute("cursos", cursoService.obtenerCursosDisponibles());
            }
        } else {
            model.addAttribute("cursos", cursoService.obtenerCursosDisponibles());
        }

        return "cursos";
    }
}