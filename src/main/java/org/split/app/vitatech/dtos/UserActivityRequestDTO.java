package org.split.app.vitatech.dtos;

import java.time.LocalDateTime;

public record UserActivityRequestDTO(
        Integer activityId,
        Integer locationId,
        Integer durationMinutes,
        LocalDateTime performedAt
) {}