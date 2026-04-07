package com.gullberg.healthtracker.model;

import com.gullberg.healthtracker.model.enums.MealType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a nutrition/meal log entry recorded by a user.
 */
@Entity
@Table(name = "nutrition_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLog extends BaseEntity {

    @NotNull(message = "Meal type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 500)
    private String description;

    @Min(value = 0, message = "Calories cannot be negative")
    private Integer calories;

    @Min(value = 0, message = "Protein cannot be negative")
    @Column(name = "protein_grams")
    private Double proteinGrams;

    @Min(value = 0, message = "Carbs cannot be negative")
    @Column(name = "carbs_grams")
    private Double carbsGrams;

    @Min(value = 0, message = "Fat cannot be negative")
    @Column(name = "fat_grams")
    private Double fatGrams;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
