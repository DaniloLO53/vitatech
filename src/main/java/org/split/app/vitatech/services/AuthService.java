package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.AuthResponseDTO;
import org.split.app.vitatech.dtos.LoginRequestDTO;
import org.split.app.vitatech.dtos.RegisterRequestDTO;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public AuthResponseDTO register(RegisterRequestDTO data) {
        if (userRepository.existsByEmail(data.email())) {
            throw new RuntimeException("Email already in use!");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = new User();
        newUser.setName(data.name());
        newUser.setEmail(data.email());
        newUser.setPasswordHash(encryptedPassword);
        newUser.setRole(data.role());

        userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);

        // Passando o ID aqui (newUser.getId())
        return new AuthResponseDTO(token, newUser.getId(), newUser.getName(), newUser.getRole().name());
    }

    public AuthResponseDTO login(LoginRequestDTO data) {
        User user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(data.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password!");
        }

        String token = tokenService.generateToken(user);

        // Passando o ID aqui (user.getId())
        return new AuthResponseDTO(token, user.getId(), user.getName(), user.getRole().name());
    }
}