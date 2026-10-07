package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Meal;
import org.split.app.vitatech.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    // 1. Histórico Completo Paginado
    // Traz todas as refeições de um utilizador ordenadas da mais recente para a mais antiga.
    // Ideal para uma secção de "Histórico Geral" no front-end.
    Page<Meal> findByUserOrderByConsumedAtDesc(User user, Pageable pageable);

    // 2. Relatório Diário / Semanal (Muito Importante)
    // Busca todas as refeições num intervalo de tempo específico.
    // O seu Service usará isto para buscar as refeições de "hoje" e somar todos os macronutrientes.
    List<Meal> findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    // 3. Segurança no acesso aos dados
    // Garante que tentamos procurar uma refeição por ID, mas apenas se pertencer àquele utilizador,
    // evitando que o Paciente A tente aceder pelo URL à refeição do Paciente B.
    Optional<Meal> findByIdAndUser(Integer id, User user);
}