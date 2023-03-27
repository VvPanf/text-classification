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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IncidentRepo incidentRepo;

    @GetMapping
    public String getAdmin(@AuthenticationPrincipal User user,
                           @RequestParam(required = false) String type,
                           Model model) {
        List<Incident> incidents = incidentRepo.findAll();
        String title = "Полный список";
        if (Objects.equals(type, "new")) {
            incidents.sort(Comparator.comparing(Incident::getTimestamp).reversed());
            title = "Новые заявки";
        }
        if (Objects.equals(type, "rejected")) {
            incidents = incidents.stream().filter(incident -> "Закрыто".equals(incident.getStatus())).collect(Collectors.toList());
            title = "Отклонённые заявки";
        }
        model.addAttribute("incidents", incidents);
        model.addAttribute("title", title);
        model.addAttribute("user", user);
        return "admin";
    }
}
