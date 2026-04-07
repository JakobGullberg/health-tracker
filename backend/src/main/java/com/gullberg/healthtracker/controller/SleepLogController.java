package com.gullberg.healthtracker.controller;

import com.gullberg.healthtracker.dto.SleepLogRequest;
import com.gullberg.healthtracker.dto.SleepLogResponse;
import com.gullberg.healthtracker.service.SleepLogService;
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
 * REST controller for sleep log endpoints.
 */
@RestController
@RequestMapping("/api/sleep-logs")
@RequiredArgsConstructor
public class SleepLogController {

    private final SleepLogService sleepLogService;

    @GetMapping
    public ResponseEntity<List<SleepLogResponse>> getAll(@RequestParam Long userId) {
        return ResponseEntity.ok(sleepLogService.getAllByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SleepLogResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sleepLogService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SleepLogResponse> create(
            @RequestParam Long userId,
            @Valid @RequestBody SleepLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sleepLogService.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SleepLogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SleepLogRequest request) {
        return ResponseEntity.ok(sleepLogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sleepLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
