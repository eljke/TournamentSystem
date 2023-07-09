package ru.eljke.tournamentsystem.controller;

import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.mapper.UserMapper;
import ru.eljke.tournamentsystem.model.User;
import ru.eljke.tournamentsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;
    ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAll() throws Exception {
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setCity("Moscow");
        user1.setSchool("Test School 1");

        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setCity("Boston");
        user2.setSchool("Test School 2");

        List<UserDTO> users = Arrays.asList(user1, user2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<UserDTO> page = new PageImpl<>(users, pageable, users.size());

        when(service.getAll(pageable).map(UserMapper.INSTANCE::userToUserDTO)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(users.size()))

                .andExpect(jsonPath("$.content[0].id").value(user1.getId()))
                .andExpect(jsonPath("$.content[0].username").value(user1.getUsername()))
                .andExpect(jsonPath("$.content[0].city").value(user1.getCity()))
                .andExpect(jsonPath("$.content[0].school").value(user1.getSchool()))
                .andExpect(jsonPath("$.content[0].grade").value(user1.getGrade()))

                .andExpect(jsonPath("$.content[1].id").value(user2.getId()))
                .andExpect(jsonPath("$.content[1].username").value(user2.getUsername()))
                .andExpect(jsonPath("$.content[1].city").value(user2.getCity()))
                .andExpect(jsonPath("$.content[1].school").value(user2.getSchool()))
                .andExpect(jsonPath("$.content[1].grade").value(user2.getGrade()));
    }

    @Test
    void testGetById() throws Exception {
        UserDTO expectedUser = new UserDTO();
        expectedUser.setId(1L);
        expectedUser.setFullname("Some Fullname Here");

        when(UserMapper.INSTANCE.userToUserDTO(service.getById(any(Long.class)))).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedUser.getId()))
                .andExpect(jsonPath("$.firstname").value(expectedUser.getFullname()));
    }

    @Test
    void testCreate() throws Exception {
        UserDTO userToCreate = new UserDTO();
        userToCreate.setId(1L);
        userToCreate.setUsername("user1");
        userToCreate.setCity("Moscow");
        userToCreate.setSchool("Test School 1");

        UserDTO createdUser = new UserDTO();
        createdUser.setId(1L);
        createdUser.setUsername("user1");
        createdUser.setCity("Moscow");
        createdUser.setSchool("Test School 1");

        when(UserMapper.INSTANCE.userToUserDTO(service.create(any(User.class)))).thenReturn(createdUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.username").value(createdUser.getUsername()))
                .andExpect(jsonPath("$.city").value(createdUser.getCity()))
                .andExpect(jsonPath("$.school").value(createdUser.getSchool()))
                .andExpect(jsonPath("$.grade").value(createdUser.getGrade()));
    }

    @Test
    void testUpdate() throws Exception {
        Long memberId = 1L;
        UserDTO userToUpdate = new UserDTO();
        userToUpdate.setId(1L);
        userToUpdate.setUsername("user1");
        userToUpdate.setCity("Moscow");
        userToUpdate.setSchool("Test School 1");

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1L);
        updatedUser.setUsername("updated_user");
        updatedUser.setCity("updated_city");
        updatedUser.setSchool("updated_school");

        updatedUser.setId(memberId);

        when(UserMapper.INSTANCE.userToUserDTO(service.getById(memberId))).thenReturn(updatedUser);
        when(UserMapper.INSTANCE.userToUserDTO(service.update(any(User.class), eq(memberId)))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.city").value(updatedUser.getCity()))
                .andExpect(jsonPath("$.school").value(updatedUser.getSchool()))
                .andExpect(jsonPath("$.grade").value(updatedUser.getGrade()));
    }

    @Test
    void testDelete() throws Exception {
        Long memberId = 1L;
        UserDTO expectedUser = new UserDTO();
        expectedUser.setId(memberId);

        when(UserMapper.INSTANCE.userToUserDTO(service.getById(memberId))).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Успешно!"));

        verify(service, times(1)).delete(memberId);
    }

}
