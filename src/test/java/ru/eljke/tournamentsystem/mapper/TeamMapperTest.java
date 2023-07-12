package ru.eljke.tournamentsystem.mapper;

import org.junit.jupiter.api.Test;
import ru.eljke.tournamentsystem.dto.TeamDTO;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TeamMapperTest {

    private final TeamMapper teamMapper = TeamMapper.INSTANCE;

    @Test
    public void testTeamToTeamDTO() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Team 1");
        team.setMembers(List.of(
                createUser("John", "Doe"),
                createUser("Jane",  "Smith")
        ));

        TeamDTO teamDTO = teamMapper.teamToTeamDTO(team);

        assertEquals(1L, teamDTO.getId());
        assertEquals("Team 1", teamDTO.getName());
        assertEquals("Doe John, Smith Jane", teamDTO.getMembers());
    }

    @Test
    public void testTeamToTeamDTONoMembers() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Team 2");
        team.setMembers(null);

        TeamDTO teamDTO = teamMapper.teamToTeamDTO(team);

        assertEquals(1L, teamDTO.getId());
        assertEquals("Team 2", teamDTO.getName());
        assertNull(teamDTO.getMembers());
    }

    private User createUser(String firstname, String lastname) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return user;
    }
}

