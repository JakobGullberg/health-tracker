package com.gullberg.healthtracker.repository;

import com.gullberg.healthtracker.model.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for SleepLog entity.
 */
@Repository
public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {

    List<SleepLog> findByUserIdOrderByDateDesc(Long userId);

    List<SleepLog> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
