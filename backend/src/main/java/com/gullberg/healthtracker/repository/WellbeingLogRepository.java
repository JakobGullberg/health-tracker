package com.gullberg.healthtracker.repository;

import com.gullberg.healthtracker.model.WellbeingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for WellbeingLog entity.
 */
@Repository
public interface WellbeingLogRepository extends JpaRepository<WellbeingLog, Long> {

    List<WellbeingLog> findByUserIdOrderByDateDesc(Long userId);

    List<WellbeingLog> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
