package ru.eljke.tournamentsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.service.TournamentService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;
    @InjectMocks
    private TournamentController tournamentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTournamentsReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.getAllTournaments(any())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.getAll(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testGetAllTournamentsWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.getAllTournaments(any())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.getAll(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testGetTournamentByIdWhenTournamentExistsReturnsTournament() {
        TournamentDTO tournamentDTO = mock(TournamentDTO.class);
        when(tournamentService.getTournamentById(1L)).thenReturn(tournamentDTO);

        ResponseEntity<TournamentDTO> response = tournamentController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO, response.getBody());
    }

    @Test
    public void testGetTournamentByIdWhenTournamentDoesNotExistReturnsNotFound() {
        when(tournamentService.getTournamentById(1L)).thenReturn(null);

        ResponseEntity<TournamentDTO> response = tournamentController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateTournamentWhenTournamentIsValidReturnsCreatedTournament() {
        Tournament tournament = mock(Tournament.class);
        TournamentDTO tournamentDTO = mock(TournamentDTO.class);
        Authentication authentication = mock(Authentication.class);
        when(tournamentService.createTournament(tournament, authentication)).thenReturn(tournamentDTO);

        ResponseEntity<TournamentDTO> response = tournamentController.create(tournament, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO, response.getBody());
    }

    @Test
    public void testCreateTournamentWhenTournamentIsInvalidReturnsForbidden() {
        Tournament tournament = mock(Tournament.class);
        Authentication authentication = mock(Authentication.class);
        when(tournamentService.createTournament(tournament, authentication)).thenReturn(null);

        ResponseEntity<TournamentDTO> response = tournamentController.create(tournament, authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testUpdateTournamentWhenTournamentExistsReturnsUpdatedTournament() {
        Tournament tournament = mock(Tournament.class);
        TournamentDTO tournamentDTO = mock(TournamentDTO.class);
        Authentication authentication = mock(Authentication.class);
        when(tournamentService.updateTournament(tournament, 1L, authentication)).thenReturn(tournamentDTO);

        ResponseEntity<TournamentDTO> response = tournamentController.update(1L, tournament, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO, response.getBody());
    }

    @Test
    public void testUpdateTournamentWhenTournamentDoesNotExistReturnsNotFound() {
        Tournament tournament = mock(Tournament.class);
        Authentication authentication = mock(Authentication.class);
        when(tournamentService.updateTournament(tournament, 1L, authentication)).thenReturn(null);

        ResponseEntity<TournamentDTO> response = tournamentController.update(1L, tournament, authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testCancelTournamentWhenTournamentExistsReturnsSuccessMessage() {
        Authentication authentication = mock(Authentication.class);

        ResponseEntity<String> response = tournamentController.cancel(1L, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully canceled tournament with id = 1", response.getBody());
        verify(tournamentService, times(1)).cancelTournamentById(1L, authentication);
    }

    @Test
    public void testDeleteTournament_WhenTournamentExists_ReturnsSuccessMessage() {
        Authentication authentication = mock(Authentication.class);

        ResponseEntity<String> response = tournamentController.delete(1L, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted tournament with id = 1", response.getBody());
        verify(tournamentService, times(1)).deleteTournamentById(1L, authentication);
    }

    @Test
    public void testFindPastTournamentsReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findPastTournaments(any())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findPastTournaments(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindPastTournaments_WhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findPastTournaments(any())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findPastTournaments(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindCurrentTournamentsReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findCurrentTournaments(any())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findCurrentTournaments(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindCurrentTournamentsWhenNoTournamentsExist_ReturnsNotFound() {
        when(tournamentService.findCurrentTournaments(any())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findCurrentTournaments(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindUpcomingTournamentsReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findUpcomingTournaments(any())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findUpcomingTournaments(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindUpcomingTournamentsWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findUpcomingTournaments(any())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findUpcomingTournaments(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindTournamentsBetweenDatesReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findTournamentsBetweenDates(any(), any(), any())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findTournamentsBetweenDates(0, 10, "id", "asc",
                "01/01/2023 00:00", "31/12/2023 23:59");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindTournamentsBetweenDatesWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findTournamentsBetweenDates(any(), any(), any())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findTournamentsBetweenDates(0, 10, "id", "asc",
                "01/01/2023 00:00", "31/12/2023 23:59");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindPastTournamentsByUserIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findPastTournamentsByUserId(any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findPastTournamentsByUserId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindPastTournamentsByUserIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findPastTournamentsByUserId(any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findPastTournamentsByUserId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindCurrentTournamentsByUserIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findCurrentTournamentsByUserId(any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findCurrentTournamentsByUserId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindCurrentTournamentsByUserIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findCurrentTournamentsByUserId(any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findCurrentTournamentsByUserId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindUpcomingTournamentsByUserIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findUpcomingTournamentsByUserId(any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findUpcomingTournamentsByUserId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindUpcomingTournamentsByUserIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findUpcomingTournamentsByUserId(any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findUpcomingTournamentsByUserId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindTournamentsBetweenDatesByUserIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findTournamentsBetweenDatesByUserId(any(), any(), any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findTournamentsBetweenDatesByUserId(0, 10, "id", "asc",
                "01/01/2023 00:00", "31/12/2023 23:59", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindTournamentsBetweenDatesByUserIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findTournamentsBetweenDatesByUserId(any(), any(), any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findTournamentsBetweenDatesByUserId(0, 10, "id", "asc",
                "01/01/2023 00:00", "31/12/2023 23:59", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindPastTournamentsByTeamIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findPastTournamentsByTeamId(any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findPastTournamentsByTeamId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindPastTournamentsByTeamIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findPastTournamentsByTeamId(any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findPastTournamentsByTeamId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindCurrentTournamentsByTeamIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findCurrentTournamentsByTeamId(any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findCurrentTournamentsByTeamId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindCurrentTournamentsByTeamIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findCurrentTournamentsByTeamId(any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findCurrentTournamentsByTeamId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindUpcomingTournamentsByTeamIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findUpcomingTournamentsByTeamId(any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findUpcomingTournamentsByTeamId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindUpcomingTournamentsByTeamIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findUpcomingTournamentsByTeamId(any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findUpcomingTournamentsByTeamId(0, 10, "id", "asc", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }

    @Test
    public void testFindTournamentsBetweenDatesByTeamIdReturnsTournamentsPage() {
        @SuppressWarnings("unchecked")
        Page<TournamentDTO> tournamentPage = mock(Page.class);
        when(tournamentService.findTournamentsBetweenDatesByTeamId(any(), any(), any(), anyLong())).thenReturn(tournamentPage);

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findTournamentsBetweenDatesByTeamId(0, 10, "id", "asc",
                "01/01/2023 00:00", "31/12/2023 23:59", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentPage, response.getBody());
    }

    @Test
    public void testFindTournamentsBetweenDatesByTeamIdWhenNoTournamentsExistReturnsNotFound() {
        when(tournamentService.findTournamentsBetweenDatesByTeamId(any(), any(), any(), anyLong())).thenReturn(Page.empty());

        ResponseEntity<Page<TournamentDTO>> response = tournamentController.findTournamentsBetweenDatesByTeamId(0, 10, "id", "asc",
                "01/01/2023 00:00", "31/12/2023 23:59", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(NullPointerException.class, () ->
                (Objects.requireNonNull(response.getBody())).isEmpty());
    }
}

