package com.gullberg.healthtracker.repository;

import com.gullberg.healthtracker.model.NutritionLog;
import com.gullberg.healthtracker.model.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for NutritionLog entity.
 */
@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {

    List<NutritionLog> findByUserIdOrderByDateDesc(Long userId);

    List<NutritionLog> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<NutritionLog> findByUserIdAndMealType(Long userId, MealType mealType);
}
