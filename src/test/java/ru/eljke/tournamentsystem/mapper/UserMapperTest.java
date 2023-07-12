package ru.eljke.tournamentsystem.mapper;

import org.junit.jupiter.api.Test;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.Role;
import ru.eljke.tournamentsystem.entity.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    public void testUserToUserDTO() {
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setBirthDate(LocalDate.of(1990, 5, 15));
        user.setRoles(Collections.singleton(Role.ADMIN));

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertEquals("Doe John", userDTO.getFullname());
        assertEquals("15/05/1990", userDTO.getBirthDate());
        assertEquals("ADMIN", userDTO.getRoles());
    }

    @Test
    public void testUserToUserDTONullDate() {
        User user = new User();
        user.setFirstname("Jane");
        user.setLastname("Smith");
        user.setBirthDate(null);
        user.setRoles(new HashSet<>());

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertEquals("Smith Jane", userDTO.getFullname());
        assertNull(userDTO.getBirthDate());
        assertEquals("", userDTO.getRoles());
    }

    @Test
    public void testUsersToUserDTOs() {
        List<User> users = List.of(
                createUser("John", "Doe", LocalDate.of(1990, 5, 15), Role.ADMIN),
                createUser("Jane", "Smith", LocalDate.of(1985, 8, 22), Role.STUDENT)
        );

        List<UserDTO> userDTOs = userMapper.usersToUserDTOs(users);

        assertEquals(2, userDTOs.size());

        UserDTO userDTO1 = userDTOs.get(0);
        assertEquals("Doe John", userDTO1.getFullname());
        assertEquals("15/05/1990", userDTO1.getBirthDate());
        assertEquals("ADMIN", userDTO1.getRoles());

        UserDTO userDTO2 = userDTOs.get(1);
        assertEquals("Smith Jane", userDTO2.getFullname());
        assertEquals("22/08/1985", userDTO2.getBirthDate());
        assertEquals("STUDENT", userDTO2.getRoles());
    }

    private User createUser(String firstname, String lastname, LocalDate birthDate, Role... roles) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setBirthDate(birthDate);
        user.setRoles(Set.of(roles));
        return user;
    }
}
