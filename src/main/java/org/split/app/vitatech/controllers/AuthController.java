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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO data) {

        try {
            AuthResponseDTO response = authService.register(data);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
        try {
            AuthResponseDTO response = authService.login(data);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}