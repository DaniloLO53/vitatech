package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.PhysicalActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalActivityRepository extends JpaRepository<PhysicalActivity, Integer> {

    // 1. Busca Textual Paginada
    // Permite que o utilizador pesquise por "corrida" ou "ciclismo" na interface
    // de forma leve e rápida (Mobile First).
    Page<PhysicalActivity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}