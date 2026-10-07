package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.MealRequestDTO;
import org.split.app.vitatech.models.Meal;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    // 1. Endpoint para registar uma refeição
    @PostMapping
    public ResponseEntity<?> registerMeal(
            @RequestBody MealRequestDTO data,
            @AuthenticationPrincipal User user) {
        try {
            Meal newMeal = mealService.registerMeal(data, user);
            return ResponseEntity.ok(newMeal);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Endpoint para buscar todas as refeições de um dia (Para o gráfico de calorias)
    @GetMapping("/daily")
    public ResponseEntity<List<Meal>> getDailyMeals(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal User user) {

        // Exemplo de uso no Postman: GET /api/meals/daily?date=2026-10-07
        List<Meal> meals = mealService.getDailyMeals(user, date);
        return ResponseEntity.ok(meals);
    }
}