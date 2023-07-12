package ru.eljke.tournamentsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.eljke.tournamentsystem.service.UserService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserAccessControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserAccessController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void banByIdShouldReturnSuccess() throws Exception {
        Long userId = 1L;
        String reason = "Some reason";

        doNothing().when(service).banById(userId, reason);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/{id}/ban", userId)
                .param("reason", reason)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully"));

        verify(service).banById(userId, reason);
    }

    @Test
    public void unbanByIdShouldReturnSuccess() throws Exception {
        Long userId = 1L;

        doNothing().when(service).unbanById(userId);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/{id}/unban", userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully"));

        verify(service).unbanById(userId);
    }

    @Test
    public void banByUsernameShouldReturnSuccess() throws Exception {
        String username = "john";
        String reason = "Some reason";

        doNothing().when(service).banByUsername(username, reason);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/@{username}/ban", username)
                .param("reason", reason)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully"));

        verify(service).banByUsername(username, reason);
    }

    @Test
    public void unbanByUsernameShouldReturnSuccess() throws Exception {
        String username = "john";

        doNothing().when(service).unbanByUsername(username);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/@{username}/unban", username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully"));

        verify(service).unbanByUsername(username);
    }
}
