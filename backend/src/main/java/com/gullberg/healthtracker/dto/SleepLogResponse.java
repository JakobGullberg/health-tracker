package com.gullberg.healthtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Response DTO for returning sleep log data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepLogResponse {

    private Long id;
    private LocalTime bedtime;
    private LocalTime wakeTime;
    private Double durationHours;
    private Integer sleepQuality;
    private String notes;
    private LocalDate date;
    private LocalDateTime createdAt;
}
