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

    @Autowired
    private org.split.app.vitatech.repositories.PatientNutritionistRepository connectionRepository;

    @Autowired
    private org.split.app.vitatech.repositories.UserRepository userRepository;

    public List<Meal> getPatientDailyMeals(User nutritionist, Integer patientId, LocalDate date) {
        if (nutritionist.getRole() != org.split.app.vitatech.models.UserRole.NUTRITIONIST) {
            throw new RuntimeException("Acesso negado: Apenas nutricionistas podem visualizar diários.");
        }

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        boolean isConnected = connectionRepository.findByPatientAndNutritionist(patient, nutritionist)
                .map(conn -> conn.getStatus() == org.split.app.vitatech.models.ConnectionStatus.ACTIVE)
                .orElse(false);

        if (!isConnected) {
            throw new RuntimeException("Você não tem permissão para aceder ao diário deste paciente.");
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return mealRepository.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(patient, startOfDay, endOfDay);
    }

    @Transactional
    public Meal registerMeal(MealRequestDTO data, User user) {

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setMealType(data.mealType());
        meal.setConsumedAt(data.consumedAt() != null ? data.consumedAt() : LocalDateTime.now());

        List<MealItem> items = new ArrayList<>();

        for (MealItemRequestDTO itemDto : data.items()) {
            Food food = foodRepository.findById(itemDto.foodId())
                    .orElseThrow(() -> new RuntimeException("Alimento com ID " + itemDto.foodId() + " não encontrado!"));

            MealItem item = new MealItem();
            item.setMeal(meal);
            item.setFood(food);
            item.setQuantityGrams(itemDto.quantityGrams());

            items.add(item);
        }

        meal.setItems(items);

        return mealRepository.save(meal);
    }

    public List<Meal> getDailyMeals(User user, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return mealRepository.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(user, startOfDay, endOfDay);
    }
}