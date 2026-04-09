package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.WorkoutRequest;
import com.gullberg.healthtracker.dto.WorkoutResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.model.Workout;
import com.gullberg.healthtracker.model.enums.WorkoutType;
import com.gullberg.healthtracker.repository.UserRepository;
import com.gullberg.healthtracker.repository.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private final String EMAIL = "test@example.com";
    private final LocalDate DATE = LocalDate.of(2026, 4, 9);

    private User buildUser() {
        User user = User.builder()
                .email(EMAIL)
                .password("hashed")
                .firstName("Test")
                .lastName("User")
                .build();
        user.setId(1L);
        return user;
    }

    private Workout buildWorkout(User user) {
        Workout workout = Workout.builder()
                .workoutType(WorkoutType.RUNNING)
                .durationMinutes(30)
                .caloriesBurned(300)
                .notes("Morning run")
                .date(DATE)
                .user(user)
                .build();
        workout.setId(1L);
        return workout;
    }

    private WorkoutRequest buildRequest() {
        return WorkoutRequest.builder()
                .workoutType(WorkoutType.RUNNING)
                .durationMinutes(30)
                .caloriesBurned(300)
                .notes("Morning run")
                .date(DATE)
                .build();
    }

    // ── getAllByUser ──────────────────────────────────────────

    @Test
    void getAllByUser_shouldReturnWorkouts_whenUserExists() {
        User user = buildUser();
        Workout workout = buildWorkout(user);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(workoutRepository.findByUserIdOrderByDateDesc(1L)).thenReturn(List.of(workout));

        List<WorkoutResponse> result = workoutService.getAllByUser(EMAIL);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWorkoutType()).isEqualTo(WorkoutType.RUNNING);
        assertThat(result.get(0).getDurationMinutes()).isEqualTo(30);
    }

    @Test
    void getAllByUser_shouldReturnEmptyList_whenNoWorkouts() {
        User user = buildUser();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(workoutRepository.findByUserIdOrderByDateDesc(1L)).thenReturn(List.of());

        List<WorkoutResponse> result = workoutService.getAllByUser(EMAIL);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllByUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.getAllByUser(EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // ── getById ──────────────────────────────────────────────

    @Test
    void getById_shouldReturnWorkout_whenExists() {
        Workout workout = buildWorkout(buildUser());

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        WorkoutResponse result = workoutService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getWorkoutType()).isEqualTo(WorkoutType.RUNNING);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Workout not found with id: 99");
    }

    // ── create ───────────────────────────────────────────────

    @Test
    void create_shouldReturnWorkoutResponse() {
        User user = buildUser();
        WorkoutRequest request = buildRequest();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(workoutRepository.save(any(Workout.class))).thenAnswer(invocation -> {
            Workout saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        WorkoutResponse result = workoutService.create(EMAIL, request);

        assertThat(result.getWorkoutType()).isEqualTo(WorkoutType.RUNNING);
        assertThat(result.getDurationMinutes()).isEqualTo(30);
        assertThat(result.getCaloriesBurned()).isEqualTo(300);
        assertThat(result.getDate()).isEqualTo(DATE);

        verify(workoutRepository).save(any(Workout.class));
    }

    // ── update ───────────────────────────────────────────────

    @Test
    void update_shouldReturnUpdatedWorkout() {
        Workout existing = buildWorkout(buildUser());
        WorkoutRequest request = WorkoutRequest.builder()
                .workoutType(WorkoutType.CYCLING)
                .durationMinutes(45)
                .caloriesBurned(400)
                .notes("Evening cycle")
                .date(DATE)
                .build();

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(workoutRepository.save(any(Workout.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WorkoutResponse result = workoutService.update(1L, request);

        assertThat(result.getWorkoutType()).isEqualTo(WorkoutType.CYCLING);
        assertThat(result.getDurationMinutes()).isEqualTo(45);
        assertThat(result.getNotes()).isEqualTo("Evening cycle");
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.update(99L, buildRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── delete ───────────────────────────────────────────────

    @Test
    void delete_shouldSucceed_whenExists() {
        when(workoutRepository.existsById(1L)).thenReturn(true);

        workoutService.delete(1L);

        verify(workoutRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(workoutRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> workoutService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(workoutRepository, never()).deleteById(any());
    }
}
