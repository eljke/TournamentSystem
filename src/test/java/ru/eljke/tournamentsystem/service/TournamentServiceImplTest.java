package ru.eljke.tournamentsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.entity.TournamentStage;
import ru.eljke.tournamentsystem.mapper.TournamentMapper;
import ru.eljke.tournamentsystem.repository.TeamRepository;
import ru.eljke.tournamentsystem.repository.TournamentRepository;
import ru.eljke.tournamentsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TournamentServiceImplTest {
    @Mock
    private TournamentRepository tournamentRepository;
    @InjectMocks
    private TournamentServiceImpl tournamentService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    private final SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ADMIN");
    private final Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password", List.of(adminAuthority));


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTournamentById() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);

        tournamentService.getTournamentById(1L);

        assertEquals(1L, tournamentDTO.getId());
    }

    @Test
    public void testCreateTournament() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        tournamentService.getUserFromAuthentication(authentication);

        TournamentDTO createdTournamentDTO = new TournamentDTO();
        createdTournamentDTO.setId(1L);
        tournamentService.createTournament(tournament, authentication);

        assertEquals(1L, createdTournamentDTO.getId());
    }

    @Test
    public void testUpdateTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setStage(TournamentStage.REGISTRATION);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        tournamentService.getUserFromAuthentication(authentication);

        TournamentDTO updatedTournamentDTO = new TournamentDTO();
        updatedTournamentDTO.setId(1L);

        tournamentService.updateTournament(existingTournament, 1L, authentication);

        assertEquals(1L, updatedTournamentDTO.getId());
    }

    @Test
    public void testCancelTournamentById() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setStage(TournamentStage.REGISTRATION);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.getUserFromAuthentication(authentication);

        TournamentDTO tournamentDTO = TournamentMapper.INSTANCE.tournamentToTournamentDTO(tournament);
        doAnswer(invocation -> {
            tournamentDTO.setStage(String.valueOf(TournamentStage.CANCELED));
            return null;
        }).when(tournamentRepository).cancelTournamentById(1L);

        tournamentService.cancelTournamentById(1L, authentication);

        assertEquals("CANCELED", tournamentDTO.getStage());
    }

    @Test
    public void testDeleteTournamentById() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(new Tournament()));

        tournamentService.getUserFromAuthentication(authentication);

        tournamentService.deleteTournamentById(1L, authentication);

        verify(tournamentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllTournaments() {
        Pageable pageable = Pageable.unpaged();
        Tournament tournament = new Tournament();
        Page<Tournament> tournamentPage = new PageImpl<>(Collections.singletonList(tournament));
        when(tournamentRepository.findAll(pageable)).thenReturn(tournamentPage);

        Page<TournamentDTO> result = new PageImpl<>(tournamentPage.stream().map(TournamentMapper.INSTANCE::tournamentToTournamentDTO).toList());

        assertEquals(1, result.getTotalElements());
        assertEquals(tournament.getId(), result.getContent().get(0).getId());
    }

    @Test
    public void testFindPastTournaments() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Tournament> tournaments = createSampleTournamentsList();
        Page<Tournament> tournamentPage = new PageImpl<>(tournaments, pageable, tournaments.size());

        when(tournamentRepository.findPastTournaments(pageable)).thenReturn(tournamentPage);

        Page<TournamentDTO> result = tournamentService.findPastTournaments(pageable);

        assertEquals(tournamentPage.getTotalElements(), result.getTotalElements());
        assertEquals(tournamentPage.getContent().size(), result.getContent().size());
    }

    @Test
    public void testFindCurrentTournaments() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Tournament> tournaments = createSampleTournamentsList();
        Page<Tournament> tournamentPage = new PageImpl<>(tournaments, pageable, tournaments.size());

        when(tournamentRepository.findCurrentTournaments(pageable)).thenReturn(tournamentPage);

        Page<TournamentDTO> result = tournamentService.findCurrentTournaments(pageable);

        assertEquals(tournamentPage.getTotalElements(), result.getTotalElements());
        assertEquals(tournamentPage.getContent().size(), result.getContent().size());
    }

    @Test
    public void testFindUpcomingTournaments() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Tournament> tournaments = createSampleTournamentsList();
        Page<Tournament> tournamentPage = new PageImpl<>(tournaments, pageable, tournaments.size());

        when(tournamentRepository.findUpcomingTournaments(pageable)).thenReturn(tournamentPage);

        Page<TournamentDTO> result = tournamentService.findUpcomingTournaments(pageable);

        assertEquals(tournamentPage.getTotalElements(), result.getTotalElements());
        assertEquals(tournamentPage.getContent().size(), result.getContent().size());
    }

    @Test
    public void testFindTournamentsBetweenDates() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 2, 1, 0, 0);
        List<Tournament> tournaments = createSampleTournamentsList();
        Page<Tournament> tournamentPage = new PageImpl<>(tournaments, pageable, tournaments.size());

        when(tournamentRepository.findTournamentsBetweenDates(pageable, startDate, endDate)).thenReturn(tournamentPage);

        Page<TournamentDTO> result = tournamentService.findTournamentsBetweenDates(pageable, startDate, endDate);

        assertEquals(tournamentPage.getTotalElements(), result.getTotalElements());
        assertEquals(tournamentPage.getContent().size(), result.getContent().size());
    }

    private List<Tournament> createSampleTournamentsList() {
        List<Tournament> tournaments = new ArrayList<>();

        Tournament tournament1 = new Tournament();
        tournament1.setId(1L);
        tournament1.setName("Tournament 1");
        tournament1.setStartDateTime(LocalDateTime.of(2023, 1, 15, 10, 0));

        Tournament tournament2 = new Tournament();
        tournament2.setId(2L);
        tournament2.setName("Tournament 2");
        tournament2.setStartDateTime(LocalDateTime.of(2023, 2, 5, 14, 30));

        tournaments.add(tournament1);
        tournaments.add(tournament2);

        return tournaments;
    }
}
