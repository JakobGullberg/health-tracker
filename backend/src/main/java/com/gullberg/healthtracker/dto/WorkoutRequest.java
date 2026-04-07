package com.gullberg.healthtracker.dto;

import com.gullberg.healthtracker.model.enums.WorkoutType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a workout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRequest {

    @NotNull(message = "Workout type is required")
    private WorkoutType workoutType;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @Min(value = 0, message = "Calories burned cannot be negative")
    private Integer caloriesBurned;

    private String notes;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
