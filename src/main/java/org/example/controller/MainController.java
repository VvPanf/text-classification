package org.example.controller;

import org.example.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String getMessage(@AuthenticationPrincipal User user,
                             Model model) {
        model.addAttribute("user", user);
        return "index";
    }

    @PostMapping
    public String postMessage(@RequestParam String text,
                              @AuthenticationPrincipal User user,
                              Model model) {
        model.addAttribute("output", text);
        return "index";
    }
}
