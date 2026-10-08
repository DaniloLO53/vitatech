package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Food> findByCreatedByIsNull(Pageable pageable);

    Page<Food> findByCreatedBy(User user, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND (f.createdBy IS NULL OR f.createdBy = :user)")
    Page<Food> searchAvailableFoodsForUser(@Param("name") String name, @Param("user") User user, Pageable pageable);

    boolean existsByNameIgnoreCaseAndCreatedBy(String name, User user);
}