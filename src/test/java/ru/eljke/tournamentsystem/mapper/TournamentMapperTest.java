package ru.eljke.tournamentsystem.mapper;

import org.junit.jupiter.api.Test;
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.entity.TournamentStage;
import ru.eljke.tournamentsystem.entity.TournamentSubject;
import ru.eljke.tournamentsystem.entity.TournamentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TournamentMapperTest {

    private final TournamentMapper tournamentMapper = TournamentMapper.INSTANCE;

    @Test
    public void testTournamentToTournamentDTO() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Chess Tournament");
        tournament.setCity("Moscow");
        tournament.setOrganizingSchool("Chess Club");
        tournament.setStartDateTime(LocalDateTime.of(2023, 7, 15, 10, 0));
        tournament.setEndDate(LocalDate.of(2023, 7, 20));
        tournament.setMinParticipants(10);
        tournament.setMaxParticipants(20);
        tournament.setMinAgeToParticipate(12);
        tournament.setMaxAgeToParticipate(18);
        tournament.setStage(TournamentStage.REGISTRATION);
        tournament.setSubject(TournamentSubject.CHESS);
        tournament.setType(TournamentType.SWISS_SYSTEM);
        tournament.setTeamSize(1);
        tournament.setIsPublic(true);

        TournamentDTO tournamentDTO = tournamentMapper.tournamentToTournamentDTO(tournament);

        assertEquals(1L, tournamentDTO.getId());
        assertEquals("Chess Tournament", tournamentDTO.getName());
        assertEquals("Moscow", tournamentDTO.getCity());
        assertEquals("Chess Club", tournamentDTO.getOrganizingSchool());
        assertEquals("15/07/2023 10:00", tournamentDTO.getStartDateTime());
        assertEquals("20/07/2023", tournamentDTO.getEndDate());
        assertEquals(10, tournamentDTO.getMinParticipants());
        assertEquals(20, tournamentDTO.getMaxParticipants());
        assertEquals(12, tournamentDTO.getMinAgeToParticipate());
        assertEquals(18, tournamentDTO.getMaxAgeToParticipate());
        assertEquals("REGISTRATION", tournamentDTO.getStage());
        assertEquals("CHESS", tournamentDTO.getSubject());
        assertEquals("SWISS_SYSTEM", tournamentDTO.getType());
        assertEquals(1, tournamentDTO.getTeamSize());
        assertEquals(true, tournamentDTO.getIsPublic());
    }

    @Test
    public void testTournamentToTournamentDTO_NullDateTime() {
        Tournament tournament = new Tournament();
        tournament.setStartDateTime(null);
        tournament.setEndDate(null);

        TournamentDTO tournamentDTO = tournamentMapper.tournamentToTournamentDTO(tournament);

        assertNull(tournamentDTO.getStartDateTime());
        assertNull(tournamentDTO.getEndDate());
    }

    @Test
    public void testTournamentsToTournamentDTOs() {
        List<Tournament> tournaments = List.of(
                createTournament("Tournament 1", LocalDateTime.of(2023, 7, 15, 10, 0)),
                createTournament("Tournament 2", LocalDateTime.of(2023, 7, 20, 15, 30))
        );

        List<TournamentDTO> tournamentDTOs = tournamentMapper.tournamentsToTournamentDTOs(tournaments);

        assertEquals(2, tournamentDTOs.size());

        TournamentDTO tournamentDTO1 = tournamentDTOs.get(0);
        assertEquals("Tournament 1", tournamentDTO1.getName());
        assertEquals("15/07/2023 10:00", tournamentDTO1.getStartDateTime());

        TournamentDTO tournamentDTO2 = tournamentDTOs.get(1);
        assertEquals("Tournament 2", tournamentDTO2.getName());
        assertEquals("20/07/2023 15:30", tournamentDTO2.getStartDateTime());
    }

    private Tournament createTournament(String name, LocalDateTime startDateTime) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setStartDateTime(startDateTime);
        return tournament;
    }
}
