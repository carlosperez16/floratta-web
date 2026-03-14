package com.floratta.service;

import com.floratta.model.entity.Curso;
import com.floratta.model.entity.ModalidadCurso;
import com.floratta.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> obtenerCursosDisponibles() {
        return cursoRepository.findByDisponibleTrueOrderByFechaCreacionDesc();
    }

    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> obtenerPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public List<Curso> obtenerPorCategoria(Long categoriaId) {
        return cursoRepository.findByCategoriaId(categoriaId);
    }

    public List<Curso> obtenerPorModalidad(ModalidadCurso modalidad) {
        return cursoRepository.findByModalidad(modalidad);
    }

    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    public Curso actualizarDisponibilidad(Long id, Boolean disponible) {
        Curso curso = cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        curso.setDisponible(disponible);
        return cursoRepository.save(curso);
    }
}