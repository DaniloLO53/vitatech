package org.split.app.vitatech.dtos;

import java.math.BigDecimal;

public record MealItemRequestDTO(
        Integer foodId,
        BigDecimal quantityGrams
) {}