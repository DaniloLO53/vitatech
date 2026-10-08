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

    Optional<PatientNutritionist> findByPatientAndNutritionist(User patient, User nutritionist);

    Page<PatientNutritionist> findByNutritionistAndStatus(User nutritionist, ConnectionStatus status, Pageable pageable);

    Optional<PatientNutritionist> findByPatientAndStatus(User patient, ConnectionStatus status);

    List<PatientNutritionist> findAllByNutritionist(User nutritionist);
}