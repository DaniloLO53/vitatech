package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.FoodRequestDTO;
import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    // 1. Endpoint para criar uma receita personalizada
    @PostMapping
    public ResponseEntity<?> createCustomFood(
            @RequestBody FoodRequestDTO data,
            @AuthenticationPrincipal User user) { // Pega o utilizador do Token JWT automaticamente!
        try {
            Food newFood = foodService.createCustomFood(data, user);
            return ResponseEntity.ok(newFood);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Endpoint para pesquisar alimentos (Com paginação)
    @GetMapping("/search")
    public ResponseEntity<Page<Food>> searchFoods(
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal User user,
            Pageable pageable) {

        // Exemplo de uso no Postman: GET /api/foods/search?name=frango&page=0&size=10
        Page<Food> foods = foodService.searchAvailableFoods(name, user, pageable);
        return ResponseEntity.ok(foods);
    }
}