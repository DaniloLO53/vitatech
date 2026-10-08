package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.UserActivityRequestDTO;
import org.split.app.vitatech.models.ActivityLocation;
import org.split.app.vitatech.models.PhysicalActivity;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserActivity;
import org.split.app.vitatech.services.PhysicalActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class PhysicalActivityController {

    @Autowired
    private PhysicalActivityService activityService;

    @PostMapping
    public ResponseEntity<?> registerActivity(
            @RequestBody UserActivityRequestDTO data,
            @AuthenticationPrincipal User user) {
        try {
            UserActivity newActivity = activityService.registerActivity(data, user);
            return ResponseEntity.ok(newActivity);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/daily")
    public ResponseEntity<List<UserActivity>> getDailyActivities(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal User user) {
        List<UserActivity> activities = activityService.getDailyActivities(user, date);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/catalog/search")
    public ResponseEntity<Page<PhysicalActivity>> searchCatalog(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(activityService.searchCatalog(name, pageable));
    }

    @GetMapping("/locations")
    public ResponseEntity<List<ActivityLocation>> getLocations() {
        return ResponseEntity.ok(activityService.getAllLocations());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientDailyActivities(
            @PathVariable Integer patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal User nutritionist) {
        try {
            List<UserActivity> activities = activityService.getPatientDailyActivities(nutritionist, patientId, date);
            return ResponseEntity.ok(activities);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}