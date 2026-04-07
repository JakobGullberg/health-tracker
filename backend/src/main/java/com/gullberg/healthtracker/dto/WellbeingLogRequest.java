package com.gullberg.healthtracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a wellbeing log.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellbeingLogRequest {

    @NotNull(message = "Mood rating is required")
    @Min(value = 1, message = "Mood rating must be between 1 and 10")
    @Max(value = 10, message = "Mood rating must be between 1 and 10")
    private Integer moodRating;

    @Min(value = 1, message = "Stress level must be between 1 and 10")
    @Max(value = 10, message = "Stress level must be between 1 and 10")
    private Integer stressLevel;

    @Min(value = 1, message = "Energy level must be between 1 and 10")
    @Max(value = 10, message = "Energy level must be between 1 and 10")
    private Integer energyLevel;

    private String notes;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
