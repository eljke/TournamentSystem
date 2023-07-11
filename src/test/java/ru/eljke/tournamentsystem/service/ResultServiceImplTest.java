package ru.eljke.tournamentsystem.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.repository.ResultRepository;

import java.util.Optional;

public class ResultServiceImplTest {
    @InjectMocks
    private ResultServiceImpl resultService;

    @Mock
    private ResultRepository resultRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetResultById_ExistingId_ReturnsResult() {
        Long resultId = 1L;
        Result expectedResult = new Result();
        Mockito.when(resultRepository.findById(resultId))
                .thenReturn(Optional.of(expectedResult));

        Result actualResult = resultService.getResultById(resultId);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetResultByIdNonExistingIdThrowsIllegalArgumentException() {
        Long resultId = 1L;
        Mockito.when(resultRepository.findById(resultId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> resultService.getResultById(resultId));
    }

    @Test
    public void testCreateResultReturnsSavedResult() {
        Result expectedResult = new Result();
        Mockito.when(resultRepository.save(expectedResult))
                .thenReturn(expectedResult);

        Result actualResult = resultService.createResult(expectedResult);

        Assertions.assertEquals(expectedResult, actualResult);
    }
}
