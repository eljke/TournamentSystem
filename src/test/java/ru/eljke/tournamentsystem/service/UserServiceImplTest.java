package ru.eljke.tournamentsystem.service;

import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.mapper.UserMapper;
import ru.eljke.tournamentsystem.entity.GradeLetter;
import ru.eljke.tournamentsystem.entity.GradeNumber;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.entity.Role;
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
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("Username");
        expectedUser.setFirstname("Firstname");
        expectedUser.setLastname("Lastname");
        expectedUser.setPatronymic("Patronymic");
        expectedUser.setBirthDate(LocalDate.of(1000, 1, 1));
        expectedUser.setCity("City");
        expectedUser.setSchool("School");
        expectedUser.setGradeNumber(GradeNumber.ELEVEN);
        expectedUser.setGradeLetter(GradeLetter.А);
        expectedUser.setRoles(Collections.singleton(Role.ADMIN));

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(expectedUser));

        UserDTO actualMember = memberService.getById(expectedUser.getId());

        assertEquals(UserMapper.INSTANCE.userToUserDTO(expectedUser), actualMember);
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setCity("Moscow");
        user1.setSchool("Test School 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setCity("Boston");
        user2.setSchool("Test School 2");


        List<User> userEntities = Arrays.asList(user1, user2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        Page<User> page = new PageImpl<>(userEntities, pageable, userEntities.size());

        when(repository.findAll(pageable)).thenReturn(page);

        Page<UserDTO> resultPage = memberService.getAll(pageable);

        assertEquals(userEntities.size(), resultPage.getTotalElements());
        assertEquals(userEntities.stream()
                .map(UserMapper.INSTANCE::userToUserDTO)
                .toList(), resultPage.getContent());
        assertEquals(pageable, resultPage.getPageable());
    }

    @Test
    void testCreate() {
        User userToCreate = new User();
        userToCreate.setSchool("School");
        when(repository.save(any(User.class))).thenReturn(userToCreate);

        UserDTO createdUser = memberService.create(userToCreate);

        assertEquals(UserMapper.INSTANCE.userToUserDTO(userToCreate), createdUser);
        verify(repository, times(1)).save(userToCreate);
    }

    @Test
    void testUpdate() {
        Long memberId = 1L;
        User existingUser = new User();
        existingUser.setId(memberId);

        User updatedUserData = new User();
        updatedUserData.setCity("City");

        when(repository.findById(memberId)).thenReturn(Optional.of(existingUser));
        when(repository.save(any(User.class))).thenReturn(existingUser);

        UserDTO updatedUser = memberService.update(updatedUserData, memberId);

        assertEquals(updatedUserData.getCity(), updatedUser.getCity());
        verify(repository, times(1)).findById(memberId);
        verify(repository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateNonExistingMember() {
        Long memberId = 1L;
        User updatedUserData = new User();

        when(repository.findById(memberId)).thenReturn(Optional.empty());

        UserDTO updatedUser = memberService.update(updatedUserData, memberId);

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
