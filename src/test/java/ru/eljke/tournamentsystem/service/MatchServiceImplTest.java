package ru.eljke.tournamentsystem.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.Match;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.mapper.MatchMapper;
import ru.eljke.tournamentsystem.repository.MatchRepository;
import ru.eljke.tournamentsystem.repository.ResultRepository;
import ru.eljke.tournamentsystem.repository.TeamRepository;
import ru.eljke.tournamentsystem.repository.TournamentRepository;
import ru.eljke.tournamentsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MatchServiceImplTest {
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ResultRepository resultRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private TournamentServiceImpl tournamentService;
    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ADMIN");
    private final Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password", List.of(adminAuthority));


    @Test
    public void testGetMatchByIdExistingMatchReturnsMatchDTO() {
        Long matchId = 1L;
        Match match = createMatch(matchId);
        MatchDTO expectedDTO = MatchMapper.INSTANCE.matchToMatchDTO(match);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        MatchDTO actualDTO = matchService.getMatchById(matchId);

        Assertions.assertEquals(expectedDTO, actualDTO);
    }

    @Test
    public void testGetMatchByIdNonExistingMatchThrowsIllegalArgumentException() {
        Long matchId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> matchService.getMatchById(matchId));
    }

    @Test
    public void testGetAllMatchesReturnsPageOfMatchDTOs() {
        Pageable pageable = mock(Pageable.class);
        Match match = createMatch(1L);
        Page<Match> matchPage = new PageImpl<>(Collections.singletonList(match));
        Page<MatchDTO> expectedDTOPage = matchPage.map(MatchMapper.INSTANCE::matchToMatchDTO);

        when(matchRepository.findAll(pageable)).thenReturn(matchPage);

        Page<MatchDTO> actualDTOPage = matchService.getAllMatches(pageable);

        Assertions.assertEquals(expectedDTOPage, actualDTOPage);
    }

    @Test
    public void testCreateMatchReturnsCreatedMatchDTO() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(2L);

        Match match = createMatch(null);
        match.setSoloParticipant1(user1);
        match.setSoloParticipant2(user2);
        match.setTeamParticipant1(team1);
        match.setTeamParticipant2(team2);

        Result savedResult = new Result();
        savedResult.setId(1L);

        Long tournamentId = 1L;
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setMatches(new ArrayList<>());

        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.<Match>getArgument(0));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(new Team()));
        when(resultRepository.save(any(Result.class))).thenReturn(savedResult);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(new Tournament()));

        UserDTO userDTO = new UserDTO();
        userDTO.setRoles("ADMIN");

        when(tournamentService.getUserFromAuthentication(authentication)).thenReturn(userDTO);

        Assertions.assertEquals(MatchMapper.INSTANCE.matchToMatchDTO(match), matchService.createMatch(match, tournamentId, authentication));
    }

    @Test
    public void testUpdateMatchByIdExistingMatchReturnsUpdatedMatchDTO() {
        Long matchId = 1L;
        Match existingMatch = createMatch(matchId);
        Match updatedMatch = createMatch(matchId);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(2L);

        updatedMatch.setSoloParticipant1(user1);
        updatedMatch.setSoloParticipant2(user2);
        updatedMatch.setTeamParticipant1(team1);
        updatedMatch.setTeamParticipant2(team2);
        updatedMatch.setDateTime(LocalDateTime.now().plusHours(1));

        Long tournamentId = 1L;
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setMatches(new ArrayList<>());

        MatchDTO expectedDTO = MatchMapper.INSTANCE.matchToMatchDTO(updatedMatch);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(existingMatch));
        when(matchRepository.save(any(Match.class))).thenReturn(updatedMatch);

        when(userRepository.findById(existingMatch.getSoloParticipant1().getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(existingMatch.getSoloParticipant2().getId())).thenReturn(Optional.of(user2));
        when(teamRepository.findById(existingMatch.getTeamParticipant1().getId())).thenReturn(Optional.of(team1));
        when(teamRepository.findById(existingMatch.getTeamParticipant2().getId())).thenReturn(Optional.of(team2));

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

        UserDTO userDTO = new UserDTO();
        userDTO.setRoles("ADMIN");

        when(tournamentService.getUserFromAuthentication(authentication)).thenReturn(userDTO);

        MatchDTO actualDTO = matchService.updateMatchById(updatedMatch, matchId, tournamentId, authentication);

        Assertions.assertEquals(expectedDTO, actualDTO);
    }

    @Test
    public void testUpdateMatchByIdNonExistingMatchThrowsIllegalArgumentException() {
        Long matchId = 1L;
        Match updatedMatch = createMatch(matchId);
        Long tournamentId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matchService.updateMatchById(updatedMatch, matchId, tournamentId, authentication));
    }

    @Test
    public void testDeleteMatchByIdExistingMatchDeletesMatch() {
        Long matchId = 1L;
        Long tournamentId = 1L;
        Match match = createMatch(matchId);
        Tournament tournament = createTournament(tournamentId);
        tournament.setMatches(new ArrayList<>());
        tournament.addMatch(match);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        UserDTO userDTO = new UserDTO();
        userDTO.setRoles("ADMIN");

        when(tournamentService.getUserFromAuthentication(authentication)).thenReturn(userDTO);

        matchService.deleteMatchById(matchId, tournamentId, authentication);

        verify(matchRepository, times(1)).deleteById(eq(matchId));
    }

    @Test
    public void testDeleteMatchByIdNonExistingMatchThrowsIllegalArgumentException() {
        Long matchId = 1L;
        Long tournamentId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        UserDTO userDTO = new UserDTO();
        userDTO.setRoles("ADMIN");

        when(tournamentService.getUserFromAuthentication(authentication)).thenReturn(userDTO);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matchService.deleteMatchById(matchId, tournamentId, authentication));
    }

    private Match createMatch(Long id) {
        Match match = new Match();
        match.setId(id);
        match.setDateTime(LocalDateTime.now());
        match.setTeamParticipant1(new Team());
        match.setTeamParticipant2(new Team());
        match.setSoloParticipant1(new User());
        match.setSoloParticipant2(new User());
        match.setResult(new Result());
        return match;
    }

    private Tournament createTournament(Long id) {
        Tournament tournament = new Tournament();
        tournament.setId(id);
        return tournament;
    }
}
