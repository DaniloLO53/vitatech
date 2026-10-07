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

    // 1. Criar Alimento Personalizado
    public Food createCustomFood(FoodRequestDTO data, User user) {
        // Valida se o utilizador já não criou uma receita com este mesmo nome
        if (foodRepository.existsByNameIgnoreCaseAndCreatedBy(data.name(), user)) {
            throw new RuntimeException("Você já possui um alimento cadastrado com este nome!");
        }

        Food food = new Food();
        food.setName(data.name());
        food.setCaloriesPer100g(data.caloriesPer100g());
        food.setProteinPer100g(data.proteinPer100g());
        food.setCarbsPer100g(data.carbsPer100g());
        food.setFatPer100g(data.fatPer100g());
        food.setCreatedBy(user); // Associa o alimento apenas a este utilizador

        return foodRepository.save(food);
    }

    // 2. Pesquisar Alimentos
    public Page<Food> searchAvailableFoods(String name, User user, Pageable pageable) {
        // Se a barra de pesquisa estiver vazia, retorna apenas a base geral do sistema
        if (name == null || name.trim().isEmpty()) {
            return foodRepository.findByCreatedByIsNull(pageable);
        }
        // Se pesquisou por algo, cruza a base do sistema com as receitas exclusivas deste utilizador
        return foodRepository.searchAvailableFoodsForUser(name, user, pageable);
    }
}