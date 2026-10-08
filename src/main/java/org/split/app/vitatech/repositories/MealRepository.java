package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Meal;
import org.split.app.vitatech.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    Page<Meal> findByUserOrderByConsumedAtDesc(User user, Pageable pageable);

    List<Meal> findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    Optional<Meal> findByIdAndUser(Integer id, User user);
}