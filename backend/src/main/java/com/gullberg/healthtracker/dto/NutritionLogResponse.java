package com.gullberg.healthtracker.dto;

import com.gullberg.healthtracker.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for returning nutrition log data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogResponse {

    private Long id;
    private MealType mealType;
    private String description;
    private Integer calories;
    private Double proteinGrams;
    private Double carbsGrams;
    private Double fatGrams;
    private LocalDate date;
    private LocalDateTime createdAt;
}
