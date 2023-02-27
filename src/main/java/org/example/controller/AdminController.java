package org.example.controller;

import org.example.model.Incident;
import org.example.model.User;
import org.example.repo.IncidentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IncidentRepo incidentRepo;

    @GetMapping
    public String getHistory(@AuthenticationPrincipal User user,
                             Model model) {
        List<Incident> incidents = incidentRepo.findAll();
        model.addAttribute("incidents", incidents);
        model.addAttribute("user", user);
        return "admin";
    }
}
