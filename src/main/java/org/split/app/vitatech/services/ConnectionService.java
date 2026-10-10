package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.ConnectionDTO;
import org.split.app.vitatech.dtos.NutritionistPatientDTO;
import org.split.app.vitatech.dtos.PatientInfoDTO;
import org.split.app.vitatech.models.ConnectionStatus;
import org.split.app.vitatech.models.PatientNutritionist;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserRole;
import org.split.app.vitatech.repositories.PatientNutritionistRepository;
import org.split.app.vitatech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private PatientNutritionistRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    public ConnectionDTO requestConnection(User patient, Integer nutritionistId) {
        if (patient.getRole() != UserRole.PATIENT) {
            throw new RuntimeException("Apenas pacientes podem solicitar acompanhamento.");
        }

        User nutritionist = userRepository.findById(nutritionistId)
                .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado."));

        if (nutritionist.getRole() != UserRole.NUTRITIONIST) {
            throw new RuntimeException("O utilizador selecionado não é um nutricionista.");
        }

        Optional<PatientNutritionist> existingConnection = connectionRepository.findByPatientAndNutritionist(patient, nutritionist);
        if (existingConnection.isPresent()) {
            throw new RuntimeException("Já existe um pedido ou vínculo com este nutricionista.");
        }

        PatientNutritionist newConnection = new PatientNutritionist();
        newConnection.setPatient(patient);
        newConnection.setNutritionist(nutritionist);
        newConnection.setStatus(ConnectionStatus.PENDING);

        newConnection = connectionRepository.save(newConnection);
        return convertToDTO(newConnection);
    }

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

    public ConnectionDTO getMyNutritionist(User patient) {
        List<PatientNutritionist> connections = connectionRepository.findByPatient(patient);

        if (connections.isEmpty()) {
            return null; // Não há conexão nenhuma
        }

        PatientNutritionist connection = connections.get(0);

        return convertToDTO(connection);
    }

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

    public List<NutritionistPatientDTO> getMyPatients(User nutritionist) {
        if (nutritionist.getRole() != UserRole.NUTRITIONIST) {
            throw new RuntimeException("Apenas nutricionistas podem aceder a esta lista.");
        }

        List<PatientNutritionist> connections = connectionRepository.findAllByNutritionist(nutritionist);

        return connections.stream().map(conn -> new NutritionistPatientDTO(
                conn.getPatient().getId(),
                conn.getStatus(),
                new PatientInfoDTO(
                        conn.getPatient().getId(),
                        conn.getPatient().getName(),
                        conn.getPatient().getEmail()
                )
        )).toList();
    }
}