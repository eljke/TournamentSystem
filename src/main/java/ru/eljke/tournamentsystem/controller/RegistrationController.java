package ru.eljke.tournamentsystem.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.service.MemberServiceImpl;


@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final MemberServiceImpl service;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new Member());

        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(@ModelAttribute("user") Member user,
                                   BindingResult bindingResult,
                                   Model model) {

        boolean hasErrors = bindingResult.hasErrors();

        if (service.getByParam(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Имя пользователя занято, выберите другое");
            hasErrors = true;
        }
        if (hasErrors) {
            return "registration";
        } else {
            service.register(user);
            return "redirect:/login";
        }
    }
}

