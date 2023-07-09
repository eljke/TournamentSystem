package ru.eljke.tournamentsystem.service;

import ru.eljke.tournamentsystem.model.GradeLetter;
import ru.eljke.tournamentsystem.model.GradeNumber;
import ru.eljke.tournamentsystem.model.User;
import ru.eljke.tournamentsystem.model.Role;
import ru.eljke.tournamentsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Long memberId = 1L;
        User expectedUser = new User();
        expectedUser.setId(memberId);
        when(repository.findById(memberId)).thenReturn(Optional.of(expectedUser));

        User actualMember = memberService.getById(memberId);

        assertEquals(expectedUser, actualMember);
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setPatronymic("Patronymic1");
        user1.setBirthDate(LocalDate.of(2000, 12, 12));
        user1.setPhone("1234567890");
        user1.setEmail("john@example.com");
        user1.setPassword("password");
        user1.setCity("Moscow");
        user1.setSchool("Test School 1");
        user1.setGradeNumber(GradeNumber.ELEVEN);
        user1.setGradeLetter(GradeLetter.А);
        user1.setRoles(Collections.singleton(Role.STUDENT));

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setFirstname("Alice");
        user2.setLastname("Smith");
        user2.setPatronymic("Patronymic2");
        user2.setBirthDate(LocalDate.of(2002, 5, 28));
        user2.setPhone("1234567890");
        user2.setEmail("alice@example.com");
        user2.setPassword("password");
        user2.setCity("Boston");
        user2.setSchool("Test School 2");
        user2.setGradeNumber(GradeNumber.EIGHT);
        user2.setGradeLetter(GradeLetter.Г);
        user2.setRoles(Collections.singleton(Role.STUDENT));


        List<User> userEntities = Arrays.asList(user1, user2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        // Создаем объект Page с помощью конструктора PageImpl
        Page<User> page = new PageImpl<>(userEntities, pageable, userEntities.size());

        when(repository.findAll(pageable)).thenReturn(page);

        Page<User> resultPage = memberService.getAll(pageable);

        assertEquals(userEntities.size(), resultPage.getTotalElements());
        assertEquals(userEntities, resultPage.getContent());
        assertEquals(pageable, resultPage.getPageable());
    }

    @Test
    void testCreate() {
        User userToCreate = new User();
        when(repository.save(any(User.class))).thenReturn(userToCreate);

        User createdUser = memberService.create(userToCreate);

        assertEquals(userToCreate, createdUser);
        verify(repository, times(1)).save(userToCreate);
    }

    @Test
    void testUpdate() {
        Long memberId = 1L;
        User existingUser = new User();
        existingUser.setId(memberId);

        User updatedUserData = new User();
        updatedUserData.setFirstname("John");
        updatedUserData.setLastname("Doe");

        when(repository.findById(memberId)).thenReturn(Optional.of(existingUser));
        when(repository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = memberService.update(updatedUserData, memberId);

        assertEquals(updatedUserData.getFirstname(), updatedUser.getFirstname());
        assertEquals(updatedUserData.getLastname(), updatedUser.getLastname());
        verify(repository, times(1)).findById(memberId);
        verify(repository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateNonExistingMember() {
        Long memberId = 1L;
        User updatedUserData = new User();

        when(repository.findById(memberId)).thenReturn(Optional.empty());

        User updatedUser = memberService.update(updatedUserData, memberId);

        assertNull(updatedUser);
        verify(repository, times(1)).findById(memberId);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void testDelete() {
        Long memberId = 1L;
        when(repository.findById(memberId)).thenReturn(Optional.of(new User()));

        memberService.delete(memberId);

        verify(repository, times(1)).deleteById(memberId);
    }

    @Test
    void testDeleteNonExistingMember() {
        Long memberId = 1L;
        when(repository.findById(memberId)).thenReturn(Optional.empty());

        memberService.delete(memberId);

        verify(repository, never()).deleteById(any());
    }
}
