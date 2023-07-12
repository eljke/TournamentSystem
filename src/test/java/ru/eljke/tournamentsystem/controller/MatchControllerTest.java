package ru.eljke.tournamentsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.entity.Match;
import ru.eljke.tournamentsystem.service.MatchService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchControllerTest {

    @Mock
    private MatchService matchService;

    private MatchController matchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        matchController = new MatchController(matchService);
    }

    @Test
    void testGetAllMatchesReturnsMatchesPage() {
        Page<MatchDTO> matchesPage = Page.empty();
        when(matchService.getAllMatches(any())).thenReturn(matchesPage);

        ResponseEntity<Page<MatchDTO>> response = matchController.getAll(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllMatchesWhenNoMatchesReturnsNotFound() {
        when(matchService.getAllMatches(any())).thenReturn(Page.empty());

        ResponseEntity<Page<MatchDTO>> response = matchController.getAll(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetMatchByIdReturnsMatch() {
        MatchDTO matchDTO = new MatchDTO();
        when(matchService.getMatchById(anyLong())).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> response = matchController.getById(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matchDTO, response.getBody());
    }

    @Test
    void testGetMatchByIdWhenMatchNotFoundReturnsNotFound() {
        when(matchService.getMatchById(anyLong())).thenReturn(null);

        ResponseEntity<MatchDTO> response = matchController.getById(1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateMatchReturnsCreatedMatch() {
        MatchDTO createdMatchDTO = new MatchDTO();
        Match match = new Match();
        Authentication auth = new TestingAuthenticationToken("user", "password");

        when(matchService.createMatch(any(), anyLong(), any(Authentication.class))).thenReturn(createdMatchDTO);

        ResponseEntity<MatchDTO> response = matchController.create(1L, match, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdMatchDTO, response.getBody());
    }

    @Test
    void testCreateMatchWhenUnauthorizedReturnsForbidden() {
        Match match = new Match();
        Authentication auth = mock(Authentication.class);

        ResponseEntity<MatchDTO> response = matchController.create(1L, match, auth);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testUpdateMatchByIdReturnsUpdatedMatch() {
        MatchDTO updatedMatchDTO = new MatchDTO();
        Match match = new Match();
        Authentication auth = new TestingAuthenticationToken("user", "password");

        when(matchService.updateMatchById(any(), anyLong(), anyLong(), any(Authentication.class))).thenReturn(updatedMatchDTO);

        ResponseEntity<MatchDTO> response = matchController.update(1L, 2L, match, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMatchDTO, response.getBody());
    }

    @Test
    void testUpdateMatchByIdWhenUnauthorizedReturnsForbidden() {
        Match match = new Match();
        Authentication auth = mock(Authentication.class);

        ResponseEntity<MatchDTO> response = matchController.update(1L, 2L, match, auth);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testDeleteMatchByIdReturnsSuccessMessage() {
        Long matchId = 2L;
        Authentication auth = new TestingAuthenticationToken("user", "password");

        ResponseEntity<String> response = matchController.delete(1L, matchId, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted match with id = " + matchId, response.getBody());
    }
}
