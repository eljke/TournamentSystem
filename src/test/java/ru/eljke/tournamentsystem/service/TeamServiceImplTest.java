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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamServiceImplTest {
    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

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
        List<Team> teams = new ArrayList<>();
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(2L);
        teams.add(team1);
        teams.add(team2);
        Page<Team> teamPage = Mockito.mock(Page.class);
        Page<TeamDTO> teamDtoPage = teamPage.map(TeamMapper.INSTANCE::teamToTeamDTO);

        Mockito.when(teamRepository.findAll(pageable)).thenReturn(teamPage);

        Page<TeamDTO> result = teamService.getAllTeams(pageable);

        Assertions.assertEquals(teamDtoPage, result);
    }

//    @Test
//    public void testCreateTeamReturnsCreatedTeamDTO() {
//        List<User> userList = new ArrayList<>();
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("us");
//
//        User user2 = new User();
//        user1.setId(2L);
//        user2.setUsername("er");
//        userList.add(user1);
//        userList.add(user2);
//
//        Team team = new Team();
//        team.setId(1L);
//        team.setMembers(userList);
//
//        TeamDTO expectedTeamDTO = TeamMapper.INSTANCE.teamToTeamDTO(team);
//        Mockito.when(userRepository.findById(1L))
//                .thenReturn(Optional.of(user1));
//        Mockito.when(teamRepository.save(team))
//                .thenReturn(team);
//
//        TeamDTO actualTeamDTO = teamService.createTeam(team);
//
//        Assertions.assertEquals(expectedTeamDTO, actualTeamDTO);
//    }

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
    public void testDeleteTeamById_ExistingId_DeletesTeam() {
        // Arrange
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(team));

        // Act
        teamService.deleteTeamById(teamId);

        // Assert
        Mockito.verify(teamRepository, Mockito.times(1))
                .deleteById(teamId);
    }

    @Test
    public void testDeleteTeamById_NonExistingId_ThrowsIllegalArgumentException() {
        // Arrange
        Long teamId = 1L;
        Mockito.when(teamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> teamService.deleteTeamById(teamId));
    }
}

