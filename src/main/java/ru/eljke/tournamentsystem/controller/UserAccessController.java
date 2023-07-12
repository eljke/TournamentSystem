package ru.eljke.tournamentsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eljke.tournamentsystem.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "User access", description = "Here we can ban/unban users by id/username")
public class UserAccessController {
    private final UserService service;

    @Operation(summary = "Ban by id", description = "Allows to ban user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PutMapping("/users/{id}/ban")
    public ResponseEntity<String> banById(@PathVariable Long id,
                                          @RequestParam(defaultValue = "", required = false) String reason) {
        service.banById(id, reason);

        return ResponseEntity.ok("Successfully");
    }

    @Operation(summary = "Unban by id", description = "Allows to unban user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PutMapping("/users/{id}/unban")
    public ResponseEntity<String> unbanById(@PathVariable Long id) {
        service.unbanById(id);

        return ResponseEntity.ok("Successfully");
    }

    @Operation(summary = "Ban by username", description = "Allows to ban user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PutMapping("/users/@{username}/ban")
    public ResponseEntity<String> banByUsername(@PathVariable String username,
                                                @RequestParam(defaultValue = "", required = false) String reason) {
        service.banByUsername(username, reason);

        return ResponseEntity.ok("Successfully");
    }
    @Operation(summary = "Unban by username", description = "Allows to unban user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PutMapping("/users/@{username}/unban")
    public ResponseEntity<String> unbanByUsername(@PathVariable String username) {
        service.unbanByUsername(username);

        return ResponseEntity.ok("Successfully");
    }
}
