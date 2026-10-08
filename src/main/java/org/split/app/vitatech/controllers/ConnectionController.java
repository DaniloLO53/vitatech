package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.ConnectionDTO;
import org.split.app.vitatech.dtos.NutritionistPatientDTO;
import org.split.app.vitatech.models.ConnectionStatus;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/request/{nutritionistId}")
    public ResponseEntity<?> requestConnection(
            @PathVariable Integer nutritionistId,
            @AuthenticationPrincipal User patient) {
        try {
            ConnectionDTO response = connectionService.requestConnection(patient, nutritionistId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/respond/{patientId}")
    public ResponseEntity<?> respondToRequest(
            @PathVariable Integer patientId,
            @RequestParam ConnectionStatus status,
            @AuthenticationPrincipal User nutritionist) {
        try {
            ConnectionDTO response = connectionService.respondToRequest(nutritionist, patientId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-nutritionist")
    public ResponseEntity<?> getMyNutritionist(@AuthenticationPrincipal User patient) {
        try {
            ConnectionDTO response = connectionService.getMyNutritionist(patient);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-patient")
    public ResponseEntity<?> getMyPatients(@AuthenticationPrincipal User nutritionist) {
        try {
            List<NutritionistPatientDTO> response = connectionService.getMyPatients(nutritionist);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}