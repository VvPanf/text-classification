package org.example.controller;

import org.example.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String getHistory(@AuthenticationPrincipal User user,
                             Model model) {
        model.addAttribute("user", user);
        return "admin";
    }
}
