package org.split.app.vitatech.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientNutritionistId implements Serializable {

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "nutritionist_id")
    private Integer nutritionistId;
}