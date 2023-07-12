package ru.eljke.tournamentsystem.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.dto.TeamDTO;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.mapper.TeamMapper;
import ru.eljke.tournamentsystem.repository.TeamRepository;
import ru.eljke.tournamentsystem.repository.UserRepository;

import java.util.Optional;

public class TeamServiceImplTest {
    @InjectMocks
    private TeamServiceImpl teamService;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private Pageable pageable;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTeamByIdExistingIdReturnsTeamDTO() {
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        TeamDTO expectedTeamDTO = TeamMapper.INSTANCE.teamToTeamDTO(team);
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(team));

        TeamDTO actualTeamDTO = teamService.getTeamById(teamId);

        Assertions.assertEquals(expectedTeamDTO, actualTeamDTO);
    }


    @Test
    public void testGetTeamByIdNonExistingIdThrowsIllegalArgumentException() {
        Long teamId = 1L;
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> teamService.getTeamById(teamId));
    }

    @Test
    public void testGetAllTeamsReturnsPageOfTeamDTO() {
        @SuppressWarnings("unchecked")
        Page<Team> teamPage = Mockito.mock(Page.class);
        Page<TeamDTO> teamDtoPage = teamPage.map(TeamMapper.INSTANCE::teamToTeamDTO);

        Mockito.when(teamRepository.findAll(pageable)).thenReturn(teamPage);

        Page<TeamDTO> result = teamService.getAllTeams(pageable);

        Assertions.assertEquals(teamDtoPage, result);
    }

    @Test
    public void testCreateTeamReturnsCreatedTeamDTO() {
        Team team = new Team();
        team.setId(1L);
        team.setName("team name");

        TeamDTO expectedTeamDTO = TeamMapper.INSTANCE.teamToTeamDTO(team);

        Mockito.when(teamRepository.save(team))
                .thenReturn(team);

        if (team.getMembers() == null) {
            Assertions.assertThrows(IllegalArgumentException.class, () ->
                teamService.createTeam(team)
            );
        } else {
            TeamDTO actualTeamDTO = teamService.createTeam(team);
            Assertions.assertEquals(expectedTeamDTO, actualTeamDTO);
        }
    }

    @Test
    public void testUpdateTeamByIdExistingIdReturnsUpdatedTeamDTO() {
        Long teamId = 1L;
        Team teamToUpdate = new Team();
        teamToUpdate.setId(teamId);
        teamToUpdate.setName("Old Name");
        Team updatedTeam = new Team();
        updatedTeam.setId(teamId);
        updatedTeam.setName("New Name");
        TeamDTO expectedTeamDTO = TeamMapper.INSTANCE.teamToTeamDTO(updatedTeam);
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(teamToUpdate));
        Mockito.when(teamRepository.save(teamToUpdate))
                .thenReturn(updatedTeam);

        TeamDTO actualTeamDTO = teamService.updateTeamById(teamToUpdate, teamId);

        Assertions.assertEquals(expectedTeamDTO, actualTeamDTO);
    }

    @Test
    public void testDeleteTeamByIdExistingIdDeletesTeam() {
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(team));

        teamService.deleteTeamById(teamId);

        Mockito.verify(teamRepository, Mockito.times(1))
                .deleteById(teamId);
    }

    @Test
    public void testDeleteTeamByIdNonExistingIdThrowsIllegalArgumentException() {
        Long teamId = 1L;
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> teamService.deleteTeamById(teamId));
    }
}

