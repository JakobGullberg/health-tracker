package com.gullberg.healthtracker.dto;

import com.gullberg.healthtracker.model.enums.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for returning workout data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponse {

    private Long id;
    private WorkoutType workoutType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private String notes;
    private LocalDate date;
    private LocalDateTime createdAt;
}
