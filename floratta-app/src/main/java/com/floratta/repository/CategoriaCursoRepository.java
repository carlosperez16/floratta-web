package com.floratta.repository;

import com.floratta.model.entity.CategoriaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoriaCursoRepository extends JpaRepository<CategoriaCurso, Long> {

    Optional<CategoriaCurso> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}