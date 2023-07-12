package ru.eljke.tournamentsystem.mapper;

import org.junit.jupiter.api.Test;
import ru.eljke.tournamentsystem.dto.ResultDTO;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResultMapperTest {

    private final ResultMapper resultMapper = ResultMapper.INSTANCE;

    @Test
    public void testResultToResultDTO() {
        User winner = createUser("John" , "Doe");
        User loser = createUser("Jane", "Smith");
        Team winnerTeam = createTeam("Team 1");
        Team loserTeam = createTeam("Team 2");

        Result result = new Result();
        result.setWinner(winner);
        result.setLoser(loser);
        result.setWinnerTeam(winnerTeam);
        result.setLoserTeam(loserTeam);
        result.setIsDraw(false);

        ResultDTO resultDTO = resultMapper.resultToResultDTO(result);

        assertEquals("Doe John", resultDTO.getWinner());
        assertEquals("Smith Jane", resultDTO.getLoser());
        assertEquals("Team 1", resultDTO.getWinnerTeam());
        assertEquals("Team 2", resultDTO.getLoserTeam());
        assertEquals(false, resultDTO.getIsDraw());
    }

    @Test
    public void testResultToResultDTONullValues() {
        Result result = new Result();
        result.setWinner(null);
        result.setLoser(null);
        result.setWinnerTeam(null);
        result.setLoserTeam(null);
        result.setIsDraw(true);

        ResultDTO resultDTO = resultMapper.resultToResultDTO(result);

        assertNull(resultDTO.getWinner());
        assertNull(resultDTO.getLoser());
        assertNull(resultDTO.getWinnerTeam());
        assertNull(resultDTO.getLoserTeam());
        assertEquals(true, resultDTO.getIsDraw());
    }

    private User createUser(String firstname, String lastname) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return user;
    }

    private Team createTeam(String name) {
        Team team = new Team();
        team.setName(name);
        return team;
    }
}
