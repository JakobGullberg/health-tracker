package com.gullberg.healthtracker.controller;

import com.gullberg.healthtracker.dto.WellbeingLogRequest;
import com.gullberg.healthtracker.dto.WellbeingLogResponse;
import com.gullberg.healthtracker.service.WellbeingLogService;
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
 * REST controller for wellbeing log endpoints.
 * The authenticated user is extracted from the JWT token.
 */
@RestController
@RequestMapping("/api/wellbeing-logs")
@RequiredArgsConstructor
public class WellbeingLogController {

    private final WellbeingLogService wellbeingLogService;

    @GetMapping
    public ResponseEntity<List<WellbeingLogResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wellbeingLogService.getAllByUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WellbeingLogResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(wellbeingLogService.getById(id));
    }

    @PostMapping
    public ResponseEntity<WellbeingLogResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WellbeingLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wellbeingLogService.create(userDetails.getUsername(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WellbeingLogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody WellbeingLogRequest request) {
        return ResponseEntity.ok(wellbeingLogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wellbeingLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
