package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.ConnectionDTO;
import org.split.app.vitatech.models.ConnectionStatus;
import org.split.app.vitatech.models.PatientNutritionist;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserRole;
import org.split.app.vitatech.repositories.PatientNutritionistRepository;
import org.split.app.vitatech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private PatientNutritionistRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Paciente envia pedido para um Nutricionista
    public ConnectionDTO requestConnection(User patient, Integer nutritionistId) {
        if (patient.getRole() != UserRole.PATIENT) {
            throw new RuntimeException("Apenas pacientes podem solicitar acompanhamento.");
        }

        User nutritionist = userRepository.findById(nutritionistId)
                .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado."));

        if (nutritionist.getRole() != UserRole.NUTRITIONIST) {
            throw new RuntimeException("O utilizador selecionado não é um nutricionista.");
        }

        // Verifica se já existe um vínculo anterior
        Optional<PatientNutritionist> existingConnection = connectionRepository.findByPatientAndNutritionist(patient, nutritionist);
        if (existingConnection.isPresent()) {
            throw new RuntimeException("Já existe um pedido ou vínculo com este nutricionista.");
        }

        PatientNutritionist newConnection = new PatientNutritionist();
        newConnection.setPatient(patient);
        newConnection.setNutritionist(nutritionist);
        newConnection.setStatus(ConnectionStatus.PENDING); // Fica pendente até o nutri aceitar

        newConnection = connectionRepository.save(newConnection);
        return convertToDTO(newConnection);
    }

    // 2. Nutricionista aceita ou rejeita o pedido
    public ConnectionDTO respondToRequest(User nutritionist, Integer patientId, ConnectionStatus newStatus) {
        if (nutritionist.getRole() != UserRole.NUTRITIONIST) {
            throw new RuntimeException("Apenas nutricionistas podem responder a pedidos.");
        }

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        PatientNutritionist connection = connectionRepository.findByPatientAndNutritionist(patient, nutritionist)
                .orElseThrow(() -> new RuntimeException("Pedido de conexão não encontrado."));

        if (newStatus != ConnectionStatus.ACTIVE && newStatus != ConnectionStatus.REJECTED) {
            throw new RuntimeException("Status inválido para resposta.");
        }

        connection.setStatus(newStatus);
        connection = connectionRepository.save(connection);

        return convertToDTO(connection);
    }

    // 3. Obter o Nutricionista atual do Paciente
    public ConnectionDTO getMyNutritionist(User patient) {
        PatientNutritionist connection = connectionRepository.findByPatientAndStatus(patient, ConnectionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Você ainda não possui um nutricionista ativo."));

        return convertToDTO(connection);
    }

    // Método auxiliar para converter a Entidade pesada num DTO leve
    private ConnectionDTO convertToDTO(PatientNutritionist connection) {
        return new ConnectionDTO(
                connection.getPatient().getId(),
                connection.getPatient().getName(),
                connection.getNutritionist().getId(),
                connection.getNutritionist().getName(),
                connection.getStatus(),
                connection.getCreatedAt()
        );
    }
}