package com.gullberg.healthtracker.dto;

import com.gullberg.healthtracker.model.enums.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a nutrition log.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogRequest {

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 0, message = "Calories cannot be negative")
    private Integer calories;

    @Min(value = 0, message = "Protein cannot be negative")
    private Double proteinGrams;

    @Min(value = 0, message = "Carbs cannot be negative")
    private Double carbsGrams;

    @Min(value = 0, message = "Fat cannot be negative")
    private Double fatGrams;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
