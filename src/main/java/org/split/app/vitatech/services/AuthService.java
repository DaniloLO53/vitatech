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

    // Lógica de Registo
    public AuthResponseDTO register(RegisterRequestDTO data) {
        // 1. Verifica se o email já existe
        if (userRepository.existsByEmail(data.email())) {
            throw new RuntimeException("Email already in use!");
        }

        // 2. Encripta a palavra-passe com BCrypt
        String encryptedPassword = passwordEncoder.encode(data.password());

        // 3. Cria o novo utilizador
        User newUser = new User();
        newUser.setName(data.name());
        newUser.setEmail(data.email());
        newUser.setPasswordHash(encryptedPassword);
        newUser.setRole(data.role());

        // 4. Guarda na base de dados
        userRepository.save(newUser);

        // 5. Gera o token para fazer login automático após o registo
        String token = tokenService.generateToken(newUser);
        return new AuthResponseDTO(token, newUser.getName(), newUser.getRole().name());
    }

    // Lógica de Login
    public AuthResponseDTO login(LoginRequestDTO data) {
        // 1. Procura o utilizador pelo email
        User user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 2. Verifica se a palavra-passe digitada coincide com o hash guardado
        if (!passwordEncoder.matches(data.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password!");
        }

        // 3. Gera e devolve o token JWT
        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token, user.getName(), user.getRole().name());
    }
}