package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.AuthResponseDTO;
import org.split.app.vitatech.dtos.LoginRequestDTO;
import org.split.app.vitatech.dtos.RegisterRequestDTO;
import org.split.app.vitatech.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint para criar uma nova conta
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO data) {
        
        try {
            AuthResponseDTO response = authService.register(data);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Em caso de erro (ex: email já existe), retorna status 400 Bad Request com a mensagem
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para fazer login e receber o Token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
        try {
            AuthResponseDTO response = authService.login(data);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Em caso de credenciais inválidas, retorna status 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}