package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.NutritionistResponseDTO;
import org.split.app.vitatech.models.UserRole;
import org.split.app.vitatech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NutritionistService {

    @Autowired
    private UserRepository userRepository;

    public Page<NutritionistResponseDTO> searchNutritionists(String name, Pageable pageable) {
        String searchTerm = (name != null) ? name : "";

        // Retorna a página de utilizadores convertida automaticamente para o formato DTO
        return userRepository.findByRoleAndNameContainingIgnoreCase(UserRole.NUTRITIONIST, searchTerm, pageable)
                .map(user -> new NutritionistResponseDTO(user.getId(), user.getName()));
    }
}