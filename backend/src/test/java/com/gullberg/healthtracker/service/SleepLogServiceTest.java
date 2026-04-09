package com.gullberg.healthtracker.service;

import com.gullberg.healthtracker.dto.SleepLogRequest;
import com.gullberg.healthtracker.dto.SleepLogResponse;
import com.gullberg.healthtracker.exception.ResourceNotFoundException;
import com.gullberg.healthtracker.model.SleepLog;
import com.gullberg.healthtracker.model.User;
import com.gullberg.healthtracker.repository.SleepLogRepository;
import com.gullberg.healthtracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SleepLogServiceTest {

    @Mock
    private SleepLogRepository sleepLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SleepLogService sleepLogService;

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

    private SleepLog buildSleepLog(User user) {
        SleepLog log = SleepLog.builder()
                .bedtime(LocalTime.of(23, 0))
                .wakeTime(LocalTime.of(7, 0))
                .durationHours(8.0)
                .sleepQuality(7)
                .notes("Slept well")
                .date(DATE)
                .user(user)
                .build();
        log.setId(1L);
        return log;
    }

    private SleepLogRequest buildRequest() {
        return SleepLogRequest.builder()
                .bedtime(LocalTime.of(23, 0))
                .wakeTime(LocalTime.of(7, 0))
                .durationHours(8.0)
                .sleepQuality(7)
                .notes("Slept well")
                .date(DATE)
                .build();
    }

    // ── getAllByUser ──────────────────────────────────────────

    @Test
    void getAllByUser_shouldReturnSleepLogs_whenUserExists() {
        User user = buildUser();
        SleepLog log = buildSleepLog(user);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(sleepLogRepository.findByUserIdOrderByDateDesc(1L)).thenReturn(List.of(log));

        List<SleepLogResponse> result = sleepLogService.getAllByUser(EMAIL);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDurationHours()).isEqualTo(8.0);
        assertThat(result.get(0).getSleepQuality()).isEqualTo(7);
    }

    @Test
    void getAllByUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepLogService.getAllByUser(EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // ── getById ──────────────────────────────────────────────

    @Test
    void getById_shouldReturnSleepLog_whenExists() {
        SleepLog log = buildSleepLog(buildUser());

        when(sleepLogRepository.findById(1L)).thenReturn(Optional.of(log));

        SleepLogResponse result = sleepLogService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBedtime()).isEqualTo(LocalTime.of(23, 0));
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(sleepLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepLogService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("SleepLog not found with id: 99");
    }

    // ── create ───────────────────────────────────────────────

    @Test
    void create_shouldReturnSleepLogResponse() {
        User user = buildUser();
        SleepLogRequest request = buildRequest();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(sleepLogRepository.save(any(SleepLog.class))).thenAnswer(invocation -> {
            SleepLog saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        SleepLogResponse result = sleepLogService.create(EMAIL, request);

        assertThat(result.getDurationHours()).isEqualTo(8.0);
        assertThat(result.getSleepQuality()).isEqualTo(7);
        assertThat(result.getDate()).isEqualTo(DATE);

        verify(sleepLogRepository).save(any(SleepLog.class));
    }

    // ── update ───────────────────────────────────────────────

    @Test
    void update_shouldReturnUpdatedSleepLog() {
        SleepLog existing = buildSleepLog(buildUser());
        SleepLogRequest request = SleepLogRequest.builder()
                .bedtime(LocalTime.of(22, 30))
                .wakeTime(LocalTime.of(6, 30))
                .durationHours(8.0)
                .sleepQuality(9)
                .notes("Great sleep")
                .date(DATE)
                .build();

        when(sleepLogRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(sleepLogRepository.save(any(SleepLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SleepLogResponse result = sleepLogService.update(1L, request);

        assertThat(result.getSleepQuality()).isEqualTo(9);
        assertThat(result.getBedtime()).isEqualTo(LocalTime.of(22, 30));
        assertThat(result.getNotes()).isEqualTo("Great sleep");
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(sleepLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepLogService.update(99L, buildRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── delete ───────────────────────────────────────────────

    @Test
    void delete_shouldSucceed_whenExists() {
        when(sleepLogRepository.existsById(1L)).thenReturn(true);

        sleepLogService.delete(1L);

        verify(sleepLogRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(sleepLogRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> sleepLogService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(sleepLogRepository, never()).deleteById(any());
    }
}
