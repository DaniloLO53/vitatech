package org.split.app.vitatech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "activity_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "effort_multiplier", nullable = false, precision = 4, scale = 2)
    private BigDecimal effortMultiplier;
}