package com.gullberg.healthtracker.controller;

import com.gullberg.healthtracker.dto.NutritionLogRequest;
import com.gullberg.healthtracker.dto.NutritionLogResponse;
import com.gullberg.healthtracker.service.NutritionLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for nutrition log endpoints.
 */
@RestController
@RequestMapping("/api/nutrition-logs")
@RequiredArgsConstructor
public class NutritionLogController {

    private final NutritionLogService nutritionLogService;

    @GetMapping
    public ResponseEntity<List<NutritionLogResponse>> getAll(@RequestParam Long userId) {
        return ResponseEntity.ok(nutritionLogService.getAllByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionLogResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(nutritionLogService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NutritionLogResponse> create(
            @RequestParam Long userId,
            @Valid @RequestBody NutritionLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nutritionLogService.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionLogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody NutritionLogRequest request) {
        return ResponseEntity.ok(nutritionLogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nutritionLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
