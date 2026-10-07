package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {

    // 1. Histórico Completo Paginado
    // Lista todas as atividades físicas que o utilizador já registou, das mais recentes para as mais antigas.
    Page<UserActivity> findByUserOrderByPerformedAtDesc(User user, Pageable pageable);

    // 2. Cálculo Calórico Diário / Semanal (Muito Importante)
    // Tal como nas refeições, permite ao back-end buscar tudo o que foi treinado
    // num determinado dia ou semana para calcular o total de calorias gastas.
    List<UserActivity> findByUserAndPerformedAtBetweenOrderByPerformedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    // 3. Segurança no Acesso (Prevenir que pacientes vejam treinos de outros)
    Optional<UserActivity> findByIdAndUser(Integer id, User user);
}