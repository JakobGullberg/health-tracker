package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.NutritionLogRequest;
import com.gullberg.healthtracker.dto.NutritionLogResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.NutritionLog;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.model.enums.MealType;
import com.gullberg.healthtracker.repository.NutritionLogRepository;
import com.gullberg.healthtracker.repository.UserRepository;
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
class NutritionLogServiceTest {

    @Mock
    private NutritionLogRepository nutritionLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NutritionLogService nutritionLogService;

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

    private NutritionLog buildNutritionLog(User user) {
        NutritionLog log = NutritionLog.builder()
                .mealType(MealType.LUNCH)
                .description("Chicken salad")
                .calories(450)
                .proteinGrams(35.0)
                .carbsGrams(20.0)
                .fatGrams(15.0)
                .date(DATE)
                .user(user)
                .build();
        log.setId(1L);
        return log;
    }

    private NutritionLogRequest buildRequest() {
        return NutritionLogRequest.builder()
                .mealType(MealType.LUNCH)
                .description("Chicken salad")
                .calories(450)
                .proteinGrams(35.0)
                .carbsGrams(20.0)
                .fatGrams(15.0)
                .date(DATE)
                .build();
    }

    // ── getAllByUser ──────────────────────────────────────────

    @Test
    void getAllByUser_shouldReturnNutritionLogs_whenUserExists() {
        User user = buildUser();
        NutritionLog log = buildNutritionLog(user);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(nutritionLogRepository.findByUserIdOrderByDateDesc(1L)).thenReturn(List.of(log));

        List<NutritionLogResponse> result = nutritionLogService.getAllByUser(EMAIL);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMealType()).isEqualTo(MealType.LUNCH);
        assertThat(result.get(0).getCalories()).isEqualTo(450);
        assertThat(result.get(0).getProteinGrams()).isEqualTo(35.0);
    }

    @Test
    void getAllByUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nutritionLogService.getAllByUser(EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // ── getById ──────────────────────────────────────────────

    @Test
    void getById_shouldReturnNutritionLog_whenExists() {
        NutritionLog log = buildNutritionLog(buildUser());

        when(nutritionLogRepository.findById(1L)).thenReturn(Optional.of(log));

        NutritionLogResponse result = nutritionLogService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Chicken salad");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(nutritionLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nutritionLogService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("NutritionLog not found with id: 99");
    }

    // ── create ───────────────────────────────────────────────

    @Test
    void create_shouldReturnNutritionLogResponse() {
        User user = buildUser();
        NutritionLogRequest request = buildRequest();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(nutritionLogRepository.save(any(NutritionLog.class))).thenAnswer(invocation -> {
            NutritionLog saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        NutritionLogResponse result = nutritionLogService.create(EMAIL, request);

        assertThat(result.getMealType()).isEqualTo(MealType.LUNCH);
        assertThat(result.getDescription()).isEqualTo("Chicken salad");
        assertThat(result.getCalories()).isEqualTo(450);
        assertThat(result.getDate()).isEqualTo(DATE);

        verify(nutritionLogRepository).save(any(NutritionLog.class));
    }

    // ── update ───────────────────────────────────────────────

    @Test
    void update_shouldReturnUpdatedNutritionLog() {
        NutritionLog existing = buildNutritionLog(buildUser());
        NutritionLogRequest request = NutritionLogRequest.builder()
                .mealType(MealType.DINNER)
                .description("Pasta bolognese")
                .calories(600)
                .proteinGrams(25.0)
                .carbsGrams(70.0)
                .fatGrams(18.0)
                .date(DATE)
                .build();

        when(nutritionLogRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(nutritionLogRepository.save(any(NutritionLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NutritionLogResponse result = nutritionLogService.update(1L, request);

        assertThat(result.getMealType()).isEqualTo(MealType.DINNER);
        assertThat(result.getDescription()).isEqualTo("Pasta bolognese");
        assertThat(result.getCalories()).isEqualTo(600);
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(nutritionLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nutritionLogService.update(99L, buildRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── delete ───────────────────────────────────────────────

    @Test
    void delete_shouldSucceed_whenExists() {
        when(nutritionLogRepository.existsById(1L)).thenReturn(true);

        nutritionLogService.delete(1L);

        verify(nutritionLogRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(nutritionLogRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> nutritionLogService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(nutritionLogRepository, never()).deleteById(any());
    }
}
