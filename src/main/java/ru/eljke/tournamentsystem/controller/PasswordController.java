package ru.eljke.tournamentsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.ChangePasswordRequest;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.mapper.UserMapper;
import ru.eljke.tournamentsystem.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "Password", description = "Here users have an opportunity to change password")
public class PasswordController {

    private final UserService service;

    @Operation(summary = "Change password", description = "Provides an ability to change password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/change-password")
    public ResponseEntity<UserDTO> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException("Authorize to change password");
        }

        String username = principal.getName();
        User user = service.getByUsername(username);

        // Проверка текущего пароля
        if (!service.isOldPasswordCorrect(user, changePasswordRequest.getCurrentPassword())) {
            throw new UnsupportedOperationException("Current password is not correct");
        }

        // Проверка подтверждения нового пароля
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new UnsupportedOperationException("Passwords do not match");
        }

        // Смена пароля
        service.changePassword(user, changePasswordRequest.getNewPassword());

        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserDTO(user));
    }
}