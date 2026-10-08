package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.NutritionistResponseDTO;
import org.split.app.vitatech.services.NutritionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nutritionists")
public class NutritionistController {

    @Autowired
    private NutritionistService nutritionistService;

    @GetMapping("/search")
    public ResponseEntity<Page<NutritionistResponseDTO>> searchNutritionists(
            @RequestParam(required = false, defaultValue = "") String name,
            Pageable pageable) {

        Page<NutritionistResponseDTO> results = nutritionistService.searchNutritionists(name, pageable);
        return ResponseEntity.ok(results);
    }
}