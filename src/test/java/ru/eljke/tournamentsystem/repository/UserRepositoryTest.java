package ru.eljke.tournamentsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.eljke.tournamentsystem.entity.GradeLetter;
import ru.eljke.tournamentsystem.entity.GradeNumber;
import ru.eljke.tournamentsystem.entity.Role;
import ru.eljke.tournamentsystem.entity.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("johndoe");
        user.setLastname("Smith");
        user.setFirstname("John");
        user.setPatronymic("Patronymic");
        user.setPassword("Password");
        user.setRoles(Collections.singleton(Role.TEACHER));
        user.setBirthDate(LocalDate.of(2000, 5, 5));
        user.setGradeNumber(GradeNumber.ELEVEN);
        user.setGradeLetter(GradeLetter.А);
        user.setCity("New York");
        user.setSchool("High School");

        entityManager.persist(user);
        entityManager.flush();

        User foundUser = userRepository.findUserByUsername("johndoe");

        assertNotNull(foundUser);
        assertEquals("johndoe", foundUser.getUsername());
    }

    @Test
    public void testFindUserByUsernameUserNotFound() {
        User foundUser = userRepository.findUserByUsername("johndoe");

        assertNull(foundUser);
    }

    @Test
    public void testSearchUsers() {
        User user1 = new User();
        user1.setLastname("Smith");
        user1.setFirstname("John");
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
        user2.setLastname("Smith");
        user2.setFirstname("Jane");
        user2.setPatronymic("Patronymic");
        user2.setUsername("janesmith");
        user2.setPassword("Password");
        user2.setRoles(Collections.singleton(Role.TEACHER));
        user2.setBirthDate(LocalDate.of(2003, 7, 3));
        user2.setGradeNumber(GradeNumber.SIX);
        user2.setGradeLetter(GradeLetter.В);
        user2.setCity("London");
        user2.setSchool("Secondary School");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> searchResults = userRepository.searchUsers("Smith");

        assertEquals(2, searchResults.size());
        assertEquals("johnsmith", searchResults.get(0).getUsername());
        assertEquals("janesmith", searchResults.get(1).getUsername());
    }

    @Test
    public void testSearchUsersNoResults() {
        List<User> searchResults = userRepository.searchUsers("Smith");

        assertEquals(0, searchResults.size());
    }
}

