package org.example.controller;

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
    public String getMessage() {
        return "index";
    }

    @PostMapping
    public String postMessage(@RequestParam String text, Model model) {
        model.addAttribute("output", text);
        return "index";
    }
}
