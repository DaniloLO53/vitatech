package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.PhysicalActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalActivityRepository extends JpaRepository<PhysicalActivity, Integer> {

    Page<PhysicalActivity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}