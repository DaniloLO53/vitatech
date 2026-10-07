package org.split.app.vitatech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "physical_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "base_calories_per_minute", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseCaloriesPerMinute;
}