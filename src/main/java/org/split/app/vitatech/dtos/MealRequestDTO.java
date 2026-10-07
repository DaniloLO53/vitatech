package org.split.app.vitatech.dtos;

import org.split.app.vitatech.models.MealType;
import java.time.LocalDateTime;
import java.util.List;

public record MealRequestDTO(
        MealType mealType,
        LocalDateTime consumedAt,
        List<MealItemRequestDTO> items
) {}