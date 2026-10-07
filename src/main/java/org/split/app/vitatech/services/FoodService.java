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
    // 2. Pesquisar Alimentos
    public Page<Food> searchAvailableFoods(String name, User user, Pageable pageable) {
        // Se o nome for nulo, converte para string vazia para a query trazer todos os itens
        String searchTerm = (name == null) ? "" : name.trim();

        // Retorna sempre a combinação: Alimentos do Sistema + Alimentos do Usuário Logado
        return foodRepository.searchAvailableFoodsForUser(searchTerm, user, pageable);
    }
}