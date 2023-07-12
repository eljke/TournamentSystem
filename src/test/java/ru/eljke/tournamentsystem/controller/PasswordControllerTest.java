package ru.eljke.tournamentsystem.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.ChangePasswordRequest;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.service.UserService;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PasswordControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private PasswordController passwordController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangePasswordSuccess() throws IllegalAccessException {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");
        changePasswordRequest.setConfirmPassword("newPassword");

        User user = new User();
        user.setUsername("username");

        when(principal.getName()).thenReturn("username");
        when(userService.getByUsername("username")).thenReturn(user);
        when(userService.isOldPasswordCorrect(user, "oldPassword")).thenReturn(true);

        ResponseEntity<UserDTO> response = passwordController.changePassword(changePasswordRequest, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService, times(1)).changePassword(user, "newPassword");
    }

    @Test
    public void testChangePasswordIncorrectCurrentPassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("wrongPassword");
        changePasswordRequest.setNewPassword("newPassword");
        changePasswordRequest.setConfirmPassword("newPassword");

        User user = new User();
        user.setUsername("username");

        when(principal.getName()).thenReturn("username");
        when(userService.getByUsername("username")).thenReturn(user);
        when(userService.isOldPasswordCorrect(user, "wrongPassword")).thenReturn(false);


        assertThrows(UnsupportedOperationException.class, () ->
                passwordController.changePassword(changePasswordRequest, principal));

        verify(userService, times(0)).changePassword(any(), any());
    }

    @Test
    public void testChangePasswordPasswordsDoNotMatch() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");
        changePasswordRequest.setConfirmPassword("differentPassword");

        User user = new User();
        user.setUsername("username");

        when(principal.getName()).thenReturn("username");
        when(userService.getByUsername("username")).thenReturn(user);
        when(userService.isOldPasswordCorrect(user, "oldPassword")).thenReturn(true);

        assertThrows(UnsupportedOperationException.class, () ->
                passwordController.changePassword(changePasswordRequest, principal));

        verify(userService, times(0)).changePassword(any(), any());
    }
}

