package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.ConnectionStatus;
import org.split.app.vitatech.models.PatientNutritionist;
import org.split.app.vitatech.models.PatientNutritionistId;
import org.split.app.vitatech.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientNutritionistRepository extends JpaRepository<PatientNutritionist, PatientNutritionistId> {

    // 1. Encontrar um vínculo específico
    // Verifica se já existe um pedido (pendente, ativo ou rejeitado) entre um paciente e um nutricionista.
    Optional<PatientNutritionist> findByPatientAndNutritionist(User patient, User nutritionist);

    // 2. Painel do Nutricionista: Listar Pacientes
    // Permite que o nutricionista liste todos os seus pacientes "ATIVOS" ou veja os pedidos "PENDENTES".
    // Usamos paginação para não sobrecarregar o painel se ele tiver muitos clientes.
    Page<PatientNutritionist> findByNutritionistAndStatus(User nutritionist, ConnectionStatus status, Pageable pageable);

    // 3. Painel do Paciente: Ver o seu Nutricionista
    // Retorna quem é o profissional que está a acompanhar o paciente atualmente (status = ACTIVE).
    Optional<PatientNutritionist> findByPatientAndStatus(User patient, ConnectionStatus status);

    List<PatientNutritionist> findAllByNutritionist(User nutritionist);
}