package ru.eljke.tournamentsystem.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.eljke.tournamentsystem.dto.LoginRequestDTO;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.service.UserService;


@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService service;

    @Operation(summary = "Register user", description = "Allows users to register")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Incorrect user data provided")
    })
    @PostMapping("")
    public ResponseEntity<UserDTO> register(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(service.register(request));
    }
}
