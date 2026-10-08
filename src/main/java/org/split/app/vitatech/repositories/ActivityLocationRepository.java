package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.ActivityLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLocationRepository extends JpaRepository<ActivityLocation, Integer> {

    List<ActivityLocation> findAllByOrderByNameAsc();

    List<ActivityLocation> findByNameContainingIgnoreCase(String name);
}