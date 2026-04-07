package com.gullberg.healthtracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a sleep log entry recorded by a user.
 */
@Entity
@Table(name = "sleep_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepLog extends BaseEntity {

    @NotNull(message = "Bedtime is required")
    @Column(nullable = false)
    private LocalTime bedtime;

    @NotNull(message = "Wake time is required")
    @Column(name = "wake_time", nullable = false)
    private LocalTime wakeTime;

    @NotNull(message = "Duration is required")
    @Min(value = 0, message = "Duration cannot be negative")
    @Column(name = "duration_hours", nullable = false)
    private Double durationHours;

    @Min(value = 1, message = "Quality must be between 1 and 10")
    @Max(value = 10, message = "Quality must be between 1 and 10")
    @Column(name = "sleep_quality")
    private Integer sleepQuality;

    @Column(length = 500)
    private String notes;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
