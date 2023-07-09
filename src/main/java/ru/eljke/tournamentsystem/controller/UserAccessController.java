package ru.eljke.tournamentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eljke.tournamentsystem.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserAccessController {
    private final UserService service;

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<String> banById(@PathVariable Long id,
                                          @RequestParam(defaultValue = "", required = false) String reason) {
        service.banById(id, reason);

        return ResponseEntity.ok("Successfully");
    }

    @PutMapping("/users/{id}/unban")
    public ResponseEntity<String> unbanById(@PathVariable Long id) {
        service.unbanById(id);

        return ResponseEntity.ok("Successfully");
    }

    @PutMapping("/users/@{username}/ban")
    public ResponseEntity<String> banByUsername(@PathVariable String username,
                                                @RequestParam(defaultValue = "", required = false) String reason) {
        service.banByUsername(username, reason);

        return ResponseEntity.ok("Successfully");
    }

    @PutMapping("/users/@{username}/unban")
    public ResponseEntity<String> unbanByUsername(@PathVariable String username) {
        service.unbanByUsername(username);

        return ResponseEntity.ok("Successfully");
    }
}
