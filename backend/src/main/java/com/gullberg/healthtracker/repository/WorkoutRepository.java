package com.gullberg.healthtracker.repository;

import com.gullberg.healthtracker.model.Workout;
import com.gullberg.healthtracker.model.enums.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Workout entity.
 */
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByUserIdOrderByDateDesc(Long userId);

    List<Workout> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Workout> findByUserIdAndWorkoutType(Long userId, WorkoutType workoutType);
}
