package ru.eljke.tournamentsystem.controller;


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

    @PostMapping("")
    public ResponseEntity<UserDTO> registry(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(service.register(request));
    }
}
