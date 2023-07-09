package ru.eljke.tournamentsystem.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eljke.tournamentsystem.model.GradeLetter;
import ru.eljke.tournamentsystem.model.GradeNumber;
import ru.eljke.tournamentsystem.model.Role;
import ru.eljke.tournamentsystem.model.User;
import ru.eljke.tournamentsystem.service.UserService;

import java.time.LocalDate;
import java.util.Collections;


@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService service;

    @GetMapping("")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("")
    public String registrationPost(@ModelAttribute("user") User user,
                                   BindingResult bindingResult,
                                   Model model) {

        boolean hasErrors = bindingResult.hasErrors();

        if (service.getByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "Имя пользователя занято, выберите другое");
            hasErrors = true;
        }
        if (hasErrors) {
            return "registration";
        } else {
            user.setFirstname("");
            user.setLastname("");
            user.setPatronymic("");
            user.setBirthDate(LocalDate.of(2000, 1, 1));
            user.setPhone("");
            user.setEmail("");
            user.setCity("");
            user.setSchool("");
            user.setGradeNumber(GradeNumber.NINE);
            user.setGradeLetter(GradeLetter.А);
            user.setGrade(user.getGrade());
            user.setRoles(Collections.singleton(Role.STUDENT));
            user.setPassword(user.getPassword());

            service.create(user);
            return "redirect:/login";
        }
    }
}
