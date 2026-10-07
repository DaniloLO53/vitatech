package org.split.app.vitatech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_nutritionist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientNutritionist {

    @EmbeddedId
    private PatientNutritionistId id = new PatientNutritionistId();

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    @MapsId("nutritionistId")
    @JoinColumn(name = "nutritionist_id")
    private User nutritionist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionStatus status = ConnectionStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}