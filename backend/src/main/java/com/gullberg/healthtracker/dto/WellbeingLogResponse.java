package com.gullberg.healthtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for returning wellbeing log data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellbeingLogResponse {

    private Long id;
    private Integer moodRating;
    private Integer stressLevel;
    private Integer energyLevel;
    private String notes;
    private LocalDate date;
    private LocalDateTime createdAt;
}
