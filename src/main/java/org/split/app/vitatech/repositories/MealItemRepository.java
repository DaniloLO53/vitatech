package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.Meal;
import org.split.app.vitatech.models.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Integer> {

    List<MealItem> findByMeal(Meal meal);

    boolean existsByFood(Food food);
}