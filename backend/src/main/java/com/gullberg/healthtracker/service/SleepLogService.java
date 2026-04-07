package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.SleepLogRequest;
import com.gullberg.healthtracker.dto.SleepLogResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.SleepLog;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.repository.SleepLogRepository;
import com.gullberg.healthtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for SleepLog CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class SleepLogService {

    private final SleepLogRepository sleepLogRepository;
    private final UserRepository userRepository;

    public List<SleepLogResponse> getAllByUser(Long userId) {
        return sleepLogRepository.findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SleepLogResponse getById(Long id) {
        SleepLog sleepLog = sleepLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SleepLog", id));
        return toResponse(sleepLog);
    }

    public SleepLogResponse create(Long userId, SleepLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        SleepLog sleepLog = SleepLog.builder()
                .bedtime(request.getBedtime())
                .wakeTime(request.getWakeTime())
                .durationHours(request.getDurationHours())
                .sleepQuality(request.getSleepQuality())
                .notes(request.getNotes())
                .date(request.getDate())
                .user(user)
                .build();

        return toResponse(sleepLogRepository.save(sleepLog));
    }

    public SleepLogResponse update(Long id, SleepLogRequest request) {
        SleepLog sleepLog = sleepLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SleepLog", id));

        sleepLog.setBedtime(request.getBedtime());
        sleepLog.setWakeTime(request.getWakeTime());
        sleepLog.setDurationHours(request.getDurationHours());
        sleepLog.setSleepQuality(request.getSleepQuality());
        sleepLog.setNotes(request.getNotes());
        sleepLog.setDate(request.getDate());

        return toResponse(sleepLogRepository.save(sleepLog));
    }

    public void delete(Long id) {
        if (!sleepLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("SleepLog", id);
        }
        sleepLogRepository.deleteById(id);
    }

    private SleepLogResponse toResponse(SleepLog sleepLog) {
        return SleepLogResponse.builder()
                .id(sleepLog.getId())
                .bedtime(sleepLog.getBedtime())
                .wakeTime(sleepLog.getWakeTime())
                .durationHours(sleepLog.getDurationHours())
                .sleepQuality(sleepLog.getSleepQuality())
                .notes(sleepLog.getNotes())
                .date(sleepLog.getDate())
                .createdAt(sleepLog.getCreatedAt())
                .build();
    }
}
