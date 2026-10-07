package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.Meal;
import org.split.app.vitatech.models.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Integer> {

    // 1. Buscar itens específicos de uma refeição
    // Útil se quiser carregar os itens separadamente ou fazer operações em lote.
    List<MealItem> findByMeal(Meal meal);

    // 2. Verificação de Vínculo com Alimento
    // Antes de permitir que um utilizador elimine uma "Receita Personalizada" que ele criou,
    // o seu sistema deve verificar se essa receita já foi consumida em alguma refeição.
    // Se retornar "true", a receita não deve ser eliminada (para não quebrar o histórico de macros).
    boolean existsByFood(Food food);
}