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

    @PostMapping
    public ResponseEntity<?> createCustomFood(
            @RequestBody FoodRequestDTO data,
            @AuthenticationPrincipal User user) {
        try {
            Food newFood = foodService.createCustomFood(data, user);
            return ResponseEntity.ok(newFood);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Food>> searchFoods(
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal User user,
            Pageable pageable) {

        Page<Food> foods = foodService.searchAvailableFoods(name, user, pageable);
        return ResponseEntity.ok(foods);
    }
}