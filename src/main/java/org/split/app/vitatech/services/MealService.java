package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.MealItemRequestDTO;
import org.split.app.vitatech.dtos.MealRequestDTO;
import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.Meal;
import org.split.app.vitatech.models.MealItem;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.repositories.FoodRepository;
import org.split.app.vitatech.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private FoodRepository foodRepository;

    // Adicione as injeções abaixo no topo da classe MealService:
    @Autowired
    private org.split.app.vitatech.repositories.PatientNutritionistRepository connectionRepository;

    @Autowired
    private org.split.app.vitatech.repositories.UserRepository userRepository;

    // Adicione o novo método:
    public List<Meal> getPatientDailyMeals(User nutritionist, Integer patientId, LocalDate date) {
        // 1. Valida se é nutricionista
        if (nutritionist.getRole() != org.split.app.vitatech.models.UserRole.NUTRITIONIST) {
            throw new RuntimeException("Acesso negado: Apenas nutricionistas podem visualizar diários.");
        }

        // 2. Busca o paciente
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        // 3. Verifica se existe um vínculo ATIVO entre eles
        boolean isConnected = connectionRepository.findByPatientAndNutritionist(patient, nutritionist)
                .map(conn -> conn.getStatus() == org.split.app.vitatech.models.ConnectionStatus.ACTIVE)
                .orElse(false);

        if (!isConnected) {
            throw new RuntimeException("Você não tem permissão para aceder ao diário deste paciente.");
        }

        // 4. Se passou pela segurança, busca as refeições
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return mealRepository.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(patient, startOfDay, endOfDay);
    }

    // 1. Registar uma nova refeição e os seus itens
    @Transactional // Protege a integridade do banco de dados em caso de erro no meio do processo
    public Meal registerMeal(MealRequestDTO data, User user) {

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setMealType(data.mealType());
        // Se não enviar data, assume que está a comer agora
        meal.setConsumedAt(data.consumedAt() != null ? data.consumedAt() : LocalDateTime.now());

        List<MealItem> items = new ArrayList<>();

        // Processa cada alimento inserido no prato
        for (MealItemRequestDTO itemDto : data.items()) {
            Food food = foodRepository.findById(itemDto.foodId())
                    .orElseThrow(() -> new RuntimeException("Alimento com ID " + itemDto.foodId() + " não encontrado!"));

            MealItem item = new MealItem();
            item.setMeal(meal); // Associa o item à refeição
            item.setFood(food);
            item.setQuantityGrams(itemDto.quantityGrams());

            items.add(item);
        }

        // Associa a lista de pratos à refeição
        meal.setItems(items);

        // O Spring fará a magia de guardar a Refeição e TODOS os Itens de uma vez só,
        // porque usámos cascade = CascadeType.ALL na entidade Meal.
        return mealRepository.save(meal);
    }

    // 2. Resgatar as calorias/macros de um dia específico (Para o gráfico/dashboard)
    public List<Meal> getDailyMeals(User user, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return mealRepository.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(user, startOfDay, endOfDay);
    }
}