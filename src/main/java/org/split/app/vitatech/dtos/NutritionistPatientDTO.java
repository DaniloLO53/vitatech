package org.split.app.vitatech.dtos;

import org.split.app.vitatech.models.ConnectionStatus;

public record NutritionistPatientDTO(
        Integer id,
        ConnectionStatus status,
        PatientInfoDTO patient
) {
}