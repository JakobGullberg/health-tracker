package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.WorkoutRequest;
import com.gullberg.healthtracker.dto.WorkoutResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.model.Workout;
import com.gullberg.healthtracker.repository.UserRepository;
import com.gullberg.healthtracker.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Workout CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public List<WorkoutResponse> getAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return workoutRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public WorkoutResponse getById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout", id));
        return toResponse(workout);
    }

    public WorkoutResponse create(String email, WorkoutRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Workout workout = Workout.builder()
                .workoutType(request.getWorkoutType())
                .durationMinutes(request.getDurationMinutes())
                .caloriesBurned(request.getCaloriesBurned())
                .notes(request.getNotes())
                .date(request.getDate())
                .user(user)
                .build();

        return toResponse(workoutRepository.save(workout));
    }

    public WorkoutResponse update(Long id, WorkoutRequest request) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout", id));

        workout.setWorkoutType(request.getWorkoutType());
        workout.setDurationMinutes(request.getDurationMinutes());
        workout.setCaloriesBurned(request.getCaloriesBurned());
        workout.setNotes(request.getNotes());
        workout.setDate(request.getDate());

        return toResponse(workoutRepository.save(workout));
    }

    public void delete(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout", id);
        }
        workoutRepository.deleteById(id);
    }

    private WorkoutResponse toResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .workoutType(workout.getWorkoutType())
                .durationMinutes(workout.getDurationMinutes())
                .caloriesBurned(workout.getCaloriesBurned())
                .notes(workout.getNotes())
                .date(workout.getDate())
                .createdAt(workout.getCreatedAt())
                .build();
    }
}
