package com.gullberg.healthtracker.controller;

import com.gullberg.healthtracker.dto.WorkoutRequest;
import com.gullberg.healthtracker.dto.WorkoutResponse;
import com.gullberg.healthtracker.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for workout endpoints.
 * The authenticated user is extracted from the JWT token.
 */
@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(workoutService.getAllByUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getById(id));
    }

    @PostMapping
    public ResponseEntity<WorkoutResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WorkoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.create(userDetails.getUsername(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutRequest request) {
        return ResponseEntity.ok(workoutService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workoutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
