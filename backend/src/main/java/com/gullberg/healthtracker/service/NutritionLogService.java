package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.NutritionLogRequest;
import com.gullberg.healthtracker.dto.NutritionLogResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.NutritionLog;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.repository.NutritionLogRepository;
import com.gullberg.healthtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for NutritionLog CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private final NutritionLogRepository nutritionLogRepository;
    private final UserRepository userRepository;

    public List<NutritionLogResponse> getAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return nutritionLogRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NutritionLogResponse getById(Long id) {
        NutritionLog log = nutritionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionLog", id));
        return toResponse(log);
    }

    public NutritionLogResponse create(String email, NutritionLogRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NutritionLog log = NutritionLog.builder()
                .mealType(request.getMealType())
                .description(request.getDescription())
                .calories(request.getCalories())
                .proteinGrams(request.getProteinGrams())
                .carbsGrams(request.getCarbsGrams())
                .fatGrams(request.getFatGrams())
                .date(request.getDate())
                .user(user)
                .build();

        return toResponse(nutritionLogRepository.save(log));
    }

    public NutritionLogResponse update(Long id, NutritionLogRequest request) {
        NutritionLog log = nutritionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionLog", id));

        log.setMealType(request.getMealType());
        log.setDescription(request.getDescription());
        log.setCalories(request.getCalories());
        log.setProteinGrams(request.getProteinGrams());
        log.setCarbsGrams(request.getCarbsGrams());
        log.setFatGrams(request.getFatGrams());
        log.setDate(request.getDate());

        return toResponse(nutritionLogRepository.save(log));
    }

    public void delete(Long id) {
        if (!nutritionLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("NutritionLog", id);
        }
        nutritionLogRepository.deleteById(id);
    }

    private NutritionLogResponse toResponse(NutritionLog log) {
        return NutritionLogResponse.builder()
                .id(log.getId())
                .mealType(log.getMealType())
                .description(log.getDescription())
                .calories(log.getCalories())
                .proteinGrams(log.getProteinGrams())
                .carbsGrams(log.getCarbsGrams())
                .fatGrams(log.getFatGrams())
                .date(log.getDate())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
