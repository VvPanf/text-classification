package org.example.controller;

import org.example.model.Incident;
import org.example.model.User;
import org.example.repo.IncidentRepo;
import org.example.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/incident")
public class IncidentController {
    @Autowired
    IncidentRepo incidentRepo;
    @Autowired
    ClassificationService classificationService;

    @GetMapping
    public String getIncident(@AuthenticationPrincipal User user,
                              @RequestParam String id,
                              Model model) {
        model.addAttribute("incident", incidentRepo.findById(Long.parseLong(id)).orElse(null));
        model.addAttribute("workGroupList", classificationService.getCategories());
        model.addAttribute("user", user);
        return "incident";
    }

    @PostMapping
    public String postIncident(@AuthenticationPrincipal User user,
                               @RequestParam String id,
                               @RequestParam String workGroup,
                               Model model) {
        Incident incident = incidentRepo.findById(Long.parseLong(id)).orElse(null);
        if (incident != null) {
            incident.setWorkGroup(workGroup);
            incidentRepo.save(incident);
        }
        model.addAttribute("user", user);
        return "redirect:admin";
    }
}
