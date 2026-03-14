package com.floratta.controller;

import com.floratta.model.entity.CategoriaCurso;
import com.floratta.model.entity.Curso;
import com.floratta.repository.CategoriaCursoRepository;
import com.floratta.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoApiController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CategoriaCursoRepository categoriaCursoRepository;

    @GetMapping("/admin/todos")
    public ResponseEntity<List<Curso>> todos() {
        return ResponseEntity.ok(cursoRepository.findAll());
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaCurso>> categorias() {
        return ResponseEntity.ok(categoriaCursoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Curso curso = construirCurso(new Curso(), body);
            return ResponseEntity.ok(cursoRepository.save(curso));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Curso> opt = cursoRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        try {
            Curso curso = construirCurso(opt.get(), body);
            return ResponseEntity.ok(cursoRepository.save(curso));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        Optional<Curso> opt = cursoRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Curso curso = opt.get();
        curso.setDisponible(false);
        cursoRepository.save(curso);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private Curso construirCurso(Curso curso, Map<String, Object> body) {
        curso.setNombre((String) body.get("nombre"));
        curso.setDescripcion((String) body.get("descripcion"));
        curso.setDuracion((String) body.get("duracion"));
        curso.setImagenUrl((String) body.get("imagenUrl"));
        curso.setPrecio(new java.math.BigDecimal(body.get("precio").toString()));

        String modalidad = (String) body.get("modalidad");
        curso.setModalidad(com.floratta.model.entity.ModalidadCurso.valueOf(modalidad));

        Object cuposObj = body.get("cupos");
        curso.setCupos(cuposObj != null ? Integer.parseInt(cuposObj.toString()) : null);

        Object catId = body.get("categoriaId");
        if (catId != null) {
            categoriaCursoRepository.findById(Long.parseLong(catId.toString()))
                .ifPresent(curso::setCategoria);
        } else {
            curso.setCategoria(null);
        }

        if (curso.getDisponible() == null) curso.setDisponible(true);
        return curso;
    }
}