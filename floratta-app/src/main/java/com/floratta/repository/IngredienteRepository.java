package com.floratta.repository;

import com.floratta.model.entity.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    List<Ingrediente> findByActivoTrue();

    List<Ingrediente> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT i FROM Ingrediente i WHERE i.activo = true AND i.stockActual <= i.stockMinimo")
    List<Ingrediente> findIngredientesBajoStock();

    boolean existsByNombreIgnoreCase(String nombre);
}