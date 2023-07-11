package ru.eljke.tournamentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.eljke.tournamentsystem.entity.ChangePasswordRequest;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PasswordController {

    private final UserService service;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        String username = principal.getName();
        User user = service.getByUsername(username);

        // Проверка текущего пароля
        if (!service.isOldPasswordCorrect(user, changePasswordRequest.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current password");
        }

        // Проверка подтверждения нового пароля
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirmation password do not match");
        }

        // Смена пароля
        service.changePassword(user, changePasswordRequest.getNewPassword());

        return ResponseEntity.ok("Password changed successfully");
    }
}