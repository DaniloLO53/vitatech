package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.UserActivityRequestDTO;
import org.split.app.vitatech.models.ActivityLocation;
import org.split.app.vitatech.models.PhysicalActivity;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserActivity;
import org.split.app.vitatech.repositories.ActivityLocationRepository;
import org.split.app.vitatech.repositories.PhysicalActivityRepository;
import org.split.app.vitatech.repositories.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhysicalActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private PhysicalActivityRepository physicalActivityRepository;

    @Autowired
    private ActivityLocationRepository activityLocationRepository;

    // 1. Registar um novo treino realizado pelo paciente
    public UserActivity registerActivity(UserActivityRequestDTO data, User user) {
        // Verifica se a atividade existe no catálogo
        PhysicalActivity activity = physicalActivityRepository.findById(data.activityId())
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada!"));

        // Verifica se o local existe
        ActivityLocation location = activityLocationRepository.findById(data.locationId())
                .orElseThrow(() -> new RuntimeException("Local não encontrado!"));

        UserActivity userActivity = new UserActivity();
        userActivity.setUser(user);
        userActivity.setActivity(activity);
        userActivity.setLocation(location);
        userActivity.setDurationMinutes(data.durationMinutes());
        userActivity.setPerformedAt(data.performedAt() != null ? data.performedAt() : LocalDateTime.now());

        return userActivityRepository.save(userActivity);
    }

    // 2. Resgatar as atividades de um dia específico (Para o gráfico de calorias gastas)
    public List<UserActivity> getDailyActivities(User user, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return userActivityRepository.findByUserAndPerformedAtBetweenOrderByPerformedAtAsc(user, startOfDay, endOfDay);
    }

    // 3. Catálogo de Atividades (Paginado para não sobrecarregar o telemóvel)
    public Page<PhysicalActivity> searchCatalog(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return physicalActivityRepository.findAll(pageable);
        }
        return physicalActivityRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // 4. Catálogo de Locais (Asfalto, Areia, Calçadão, etc.)
    public List<ActivityLocation> getAllLocations() {
        return activityLocationRepository.findAllByOrderByNameAsc();
    }
}