package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.FoodRequestDTO;
import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public Food createCustomFood(FoodRequestDTO data, User user) {
        if (foodRepository.existsByNameIgnoreCaseAndCreatedBy(data.name(), user)) {
            throw new RuntimeException("Você já possui um alimento cadastrado com este nome!");
        }

        Food food = new Food();
        food.setName(data.name());
        food.setCaloriesPer100g(data.caloriesPer100g());
        food.setProteinPer100g(data.proteinPer100g());
        food.setCarbsPer100g(data.carbsPer100g());
        food.setFatPer100g(data.fatPer100g());
        food.setCreatedBy(user);

        return foodRepository.save(food);
    }

    public Page<Food> searchAvailableFoods(String name, User user, Pageable pageable) {
        String searchTerm = (name == null) ? "" : name.trim();

        return foodRepository.searchAvailableFoodsForUser(searchTerm, user, pageable);
    }
}