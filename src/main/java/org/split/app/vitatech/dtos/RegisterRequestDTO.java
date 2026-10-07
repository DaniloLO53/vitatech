package org.split.app.vitatech.dtos;

import org.split.app.vitatech.models.UserRole;

public record RegisterRequestDTO(String name, String email, String password, UserRole role) {
}