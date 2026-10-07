package org.split.app.vitatech.dtos;

import org.split.app.vitatech.models.ConnectionStatus;

public record NutritionistPatientDTO(
        Integer id, // Usado pelo front-end no handleUpdateStatus (Mapeado para o ID do paciente)
        ConnectionStatus status,
        PatientInfoDTO patient
) {
}