package ru.eljke.tournamentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eljke.tournamentsystem.service.MemberServiceImpl;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MemberServiceImpl service;

    @GetMapping("/home")
    public String admin() {
        return "home page";
    }
}
