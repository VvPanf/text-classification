package org.example.controller;

import org.example.model.User;
import org.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String getRegistration(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:login";
        }
        return "registration";
    }

    @PostMapping
    public String postRegistration(User user, Model model){
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Такой пользователь уже существует");
            return "registration";
        }
        userRepo.save(user);
        return "redirect:login";
    }

}
