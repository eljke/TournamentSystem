package ru.eljke.tournamentsystem.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class TournamentRepositoryTest {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Test
    public void testCancelTournamentById() {
        Tournament tournament = new Tournament();
        tournament.setStage(TournamentStage.CANCELED);
        tournamentRepository.save(tournament);
        Long tournamentId = tournament.getId();

        tournamentRepository.cancelTournamentById(tournamentId);

        Tournament canceledTournament = tournamentRepository.findById(tournamentId).orElse(null);
        Assertions.assertNotNull(canceledTournament);
        Assertions.assertEquals(TournamentStage.CANCELED, canceledTournament.getStage());
    }

    @Test
    public void testFindPastTournamentsByUser() {
        User user1 = new User();
        user1.setUsername("john.doe");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setPatronymic("Patronymic");
        user1.setUsername("johnsmith");
        user1.setPassword("Password");
        user1.setRoles(Collections.singleton(Role.TEACHER));
        user1.setBirthDate(LocalDate.of(2000, 5, 5));
        user1.setGradeNumber(GradeNumber.ELEVEN);
        user1.setGradeLetter(GradeLetter.А);
        user1.setCity("New York");
        user1.setSchool("High School");

        User user2 = new User();
        user2.setUsername("don.joe");
        user2.setFirstname("Don");
        user2.setLastname("Joe");
        user2.setPatronymic("Patronymic");
        user2.setUsername("janesmith");
        user2.setPassword("Password");
        user2.setRoles(Collections.singleton(Role.TEACHER));
        user2.setBirthDate(LocalDate.of(2003, 7, 3));
        user2.setGradeNumber(GradeNumber.SIX);
        user2.setGradeLetter(GradeLetter.В);
        user2.setCity("London");
        user2.setSchool("Secondary School");

        Set<User> userList = new HashSet<>();
        userList.add(user1);
        userList.add(user2);

        Tournament tournament1 = new Tournament();
        tournament1.setEndDate(LocalDate.from(LocalDateTime.now().minusDays(1)));
        tournament1.setSoloParticipants(userList);
        tournamentRepository.save(tournament1);

        Tournament tournament2 = new Tournament();
        tournament2.setEndDate(LocalDate.from(LocalDateTime.now().minusDays(2)));
        tournament2.setSoloParticipants(userList);
        tournamentRepository.save(tournament2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Tournament> result = tournamentRepository.findPastTournamentsByUser(pageable, user1);

        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(tournament1.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(tournament2.getId(), result.getContent().get(1).getId());
    }
}

