package com.floratta.repository;

import com.floratta.model.entity.Curso;
import com.floratta.model.entity.ModalidadCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByDisponibleTrue();

    List<Curso> findByCategoriaId(Long categoriaId);

    List<Curso> findByModalidad(ModalidadCurso modalidad);

    List<Curso> findByDisponibleTrueOrderByFechaCreacionDesc();
}