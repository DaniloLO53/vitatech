package org.split.app.vitatech.dtos;

import org.split.app.vitatech.models.ConnectionStatus;

import java.time.LocalDateTime;

public record ConnectionDTO(
        Integer patientId,
        String patientName,
        Integer nutritionistId,
        String nutritionistName,
        ConnectionStatus status,
        LocalDateTime createdAt
) {}