package com.gullberg.healthtracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for creating or updating a sleep log.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepLogRequest {

    @NotNull(message = "Bedtime is required")
    private LocalTime bedtime;

    @NotNull(message = "Wake time is required")
    private LocalTime wakeTime;

    @NotNull(message = "Duration is required")
    @Min(value = 0, message = "Duration cannot be negative")
    private Double durationHours;

    @Min(value = 1, message = "Quality must be between 1 and 10")
    @Max(value = 10, message = "Quality must be between 1 and 10")
    private Integer sleepQuality;

    private String notes;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
