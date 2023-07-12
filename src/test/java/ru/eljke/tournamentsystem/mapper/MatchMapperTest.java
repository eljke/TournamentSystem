package ru.eljke.tournamentsystem.mapper;

import org.junit.jupiter.api.Test;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.entity.Match;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MatchMapperTest {

    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    @Test
    public void testMatchToMatchDTO() {
        Result result = createResult();

        Match match = new Match();
        match.setDateTime(LocalDateTime.of(2023, 7, 15, 10, 0));
        match.setResult(result);

        MatchDTO matchDTO = matchMapper.matchToMatchDTO(match);

        assertEquals("15/07/2023 10:00", matchDTO.getDateTime());
        assertEquals("Doe John", matchDTO.getResult().getWinner());
        assertEquals("Smith Jane", matchDTO.getResult().getLoser());
        assertNull(matchDTO.getTeam1());
        assertNull(matchDTO.getTeam2());
        assertEquals("Doe John", matchDTO.getResult().getWinner());
        assertEquals("Smith Jane", matchDTO.getResult().getLoser());
        assertNull(matchDTO.getResult().getWinnerTeam());
        assertNull(matchDTO.getResult().getLoserTeam());
        assertEquals(false, matchDTO.getResult().getIsDraw());
    }

    @Test
    public void testMatchToMatchDTONullValues() {
        Match match = new Match();
        match.setDateTime(null);
        match.setSoloParticipant1(null);
        match.setSoloParticipant2(null);
        match.setTeamParticipant1(null);
        match.setTeamParticipant2(null);
        match.setResult(null);

        MatchDTO matchDTO = matchMapper.matchToMatchDTO(match);

        assertNull(matchDTO.getDateTime());
        assertNull(matchDTO.getParticipant1());
        assertNull(matchDTO.getParticipant2());
        assertNull(matchDTO.getTeam1());
        assertNull(matchDTO.getTeam2());
        assertNull(matchDTO.getResult());
    }

    private User createUser(String firstname, String lastname) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return user;
    }

    private Result createResult() {
        Result result = new Result();
        result.setWinner(createUser("John", "Doe"));
        result.setLoser(createUser("Jane", "Smith"));
        result.setWinnerTeam(new Team());
        result.setLoserTeam(new Team());
        result.setIsDraw(false);
        return result;
    }
}

