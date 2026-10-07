package org.split.app.vitatech.dtos;

import java.math.BigDecimal;

public record FoodRequestDTO(
        String name,
        BigDecimal caloriesPer100g,
        BigDecimal proteinPer100g,
        BigDecimal carbsPer100g,
        BigDecimal fatPer100g
) {}