package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.WellbeingLogRequest;
import com.gullberg.healthtracker.dto.WellbeingLogResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.model.WellbeingLog;
import com.gullberg.healthtracker.repository.UserRepository;
import com.gullberg.healthtracker.repository.WellbeingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for WellbeingLog CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class WellbeingLogService {

    private final WellbeingLogRepository wellbeingLogRepository;
    private final UserRepository userRepository;

    public List<WellbeingLogResponse> getAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return wellbeingLogRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public WellbeingLogResponse getById(Long id) {
        WellbeingLog log = wellbeingLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WellbeingLog", id));
        return toResponse(log);
    }

    public WellbeingLogResponse create(String email, WellbeingLogRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WellbeingLog log = WellbeingLog.builder()
                .moodRating(request.getMoodRating())
                .stressLevel(request.getStressLevel())
                .energyLevel(request.getEnergyLevel())
                .notes(request.getNotes())
                .date(request.getDate())
                .user(user)
                .build();

        return toResponse(wellbeingLogRepository.save(log));
    }

    public WellbeingLogResponse update(Long id, WellbeingLogRequest request) {
        WellbeingLog log = wellbeingLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WellbeingLog", id));

        log.setMoodRating(request.getMoodRating());
        log.setStressLevel(request.getStressLevel());
        log.setEnergyLevel(request.getEnergyLevel());
        log.setNotes(request.getNotes());
        log.setDate(request.getDate());

        return toResponse(wellbeingLogRepository.save(log));
    }

    public void delete(Long id) {
        if (!wellbeingLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("WellbeingLog", id);
        }
        wellbeingLogRepository.deleteById(id);
    }

    private WellbeingLogResponse toResponse(WellbeingLog log) {
        return WellbeingLogResponse.builder()
                .id(log.getId())
                .moodRating(log.getMoodRating())
                .stressLevel(log.getStressLevel())
                .energyLevel(log.getEnergyLevel())
                .notes(log.getNotes())
                .date(log.getDate())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
