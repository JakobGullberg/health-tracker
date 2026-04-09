package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.WellbeingLogRequest;
import com.gullberg.healthtracker.dto.WellbeingLogResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.model.WellbeingLog;
import com.gullberg.healthtracker.repository.UserRepository;
import com.gullberg.healthtracker.repository.WellbeingLogRepository;
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
class WellbeingLogServiceTest {

    @Mock
    private WellbeingLogRepository wellbeingLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WellbeingLogService wellbeingLogService;

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

    private WellbeingLog buildWellbeingLog(User user) {
        WellbeingLog log = WellbeingLog.builder()
                .moodRating(8)
                .stressLevel(4)
                .energyLevel(7)
                .notes("Feeling good")
                .date(DATE)
                .user(user)
                .build();
        log.setId(1L);
        return log;
    }

    private WellbeingLogRequest buildRequest() {
        return WellbeingLogRequest.builder()
                .moodRating(8)
                .stressLevel(4)
                .energyLevel(7)
                .notes("Feeling good")
                .date(DATE)
                .build();
    }

    // ── getAllByUser ──────────────────────────────────────────

    @Test
    void getAllByUser_shouldReturnWellbeingLogs_whenUserExists() {
        User user = buildUser();
        WellbeingLog log = buildWellbeingLog(user);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(wellbeingLogRepository.findByUserIdOrderByDateDesc(1L)).thenReturn(List.of(log));

        List<WellbeingLogResponse> result = wellbeingLogService.getAllByUser(EMAIL);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMoodRating()).isEqualTo(8);
        assertThat(result.get(0).getStressLevel()).isEqualTo(4);
        assertThat(result.get(0).getEnergyLevel()).isEqualTo(7);
    }

    @Test
    void getAllByUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wellbeingLogService.getAllByUser(EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // ── getById ──────────────────────────────────────────────

    @Test
    void getById_shouldReturnWellbeingLog_whenExists() {
        WellbeingLog log = buildWellbeingLog(buildUser());

        when(wellbeingLogRepository.findById(1L)).thenReturn(Optional.of(log));

        WellbeingLogResponse result = wellbeingLogService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMoodRating()).isEqualTo(8);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(wellbeingLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wellbeingLogService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("WellbeingLog not found with id: 99");
    }

    // ── create ───────────────────────────────────────────────

    @Test
    void create_shouldReturnWellbeingLogResponse() {
        User user = buildUser();
        WellbeingLogRequest request = buildRequest();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(wellbeingLogRepository.save(any(WellbeingLog.class))).thenAnswer(invocation -> {
            WellbeingLog saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        WellbeingLogResponse result = wellbeingLogService.create(EMAIL, request);

        assertThat(result.getMoodRating()).isEqualTo(8);
        assertThat(result.getStressLevel()).isEqualTo(4);
        assertThat(result.getEnergyLevel()).isEqualTo(7);
        assertThat(result.getDate()).isEqualTo(DATE);

        verify(wellbeingLogRepository).save(any(WellbeingLog.class));
    }

    // ── update ───────────────────────────────────────────────

    @Test
    void update_shouldReturnUpdatedWellbeingLog() {
        WellbeingLog existing = buildWellbeingLog(buildUser());
        WellbeingLogRequest request = WellbeingLogRequest.builder()
                .moodRating(6)
                .stressLevel(7)
                .energyLevel(5)
                .notes("Rough day")
                .date(DATE)
                .build();

        when(wellbeingLogRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(wellbeingLogRepository.save(any(WellbeingLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WellbeingLogResponse result = wellbeingLogService.update(1L, request);

        assertThat(result.getMoodRating()).isEqualTo(6);
        assertThat(result.getStressLevel()).isEqualTo(7);
        assertThat(result.getNotes()).isEqualTo("Rough day");
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(wellbeingLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wellbeingLogService.update(99L, buildRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── delete ───────────────────────────────────────────────

    @Test
    void delete_shouldSucceed_whenExists() {
        when(wellbeingLogRepository.existsById(1L)).thenReturn(true);

        wellbeingLogService.delete(1L);

        verify(wellbeingLogRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(wellbeingLogRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> wellbeingLogService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(wellbeingLogRepository, never()).deleteById(any());
    }
}
