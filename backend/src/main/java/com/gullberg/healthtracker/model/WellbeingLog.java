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

/**
 * Represents a daily wellbeing check-in recorded by a user.
 * Tracks mood, stress level, energy level, and optional notes.
 */
@Entity
@Table(name = "wellbeing_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellbeingLog extends BaseEntity {

    @NotNull(message = "Mood rating is required")
    @Min(value = 1, message = "Mood rating must be between 1 and 10")
    @Max(value = 10, message = "Mood rating must be between 1 and 10")
    @Column(name = "mood_rating", nullable = false)
    private Integer moodRating;

    @Min(value = 1, message = "Stress level must be between 1 and 10")
    @Max(value = 10, message = "Stress level must be between 1 and 10")
    @Column(name = "stress_level")
    private Integer stressLevel;

    @Min(value = 1, message = "Energy level must be between 1 and 10")
    @Max(value = 10, message = "Energy level must be between 1 and 10")
    @Column(name = "energy_level")
    private Integer energyLevel;

    @Column(length = 1000)
    private String notes;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
