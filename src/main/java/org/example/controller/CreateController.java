package org.example.controller;

import org.example.model.Incident;
import org.example.model.User;
import org.example.repo.IncidentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/create")
public class CreateController {
    @Autowired
    private IncidentRepo incidentRepo;

    @GetMapping
    public String getCreate(@AuthenticationPrincipal User user,    // Информация о пользователе с формы
                             Model model) {                         // Модель для установки в неё параметров
        model.addAttribute("user", user);
        return "create";
    }

    @PostMapping
    public String postCreate(@AuthenticationPrincipal User user,   // Информация о пользователе с формы
                              @Valid Incident incident,             // Инцедент с формы
                              BindingResult bindingResult,          // Информация о прохождении валидации полей с формы
                              Model model) {                        // Модель для установки в неё параметров
        if (bindingResult.hasErrors()) {
            // Если какие-то ошибки, то выводим их на форму
            model.addAttribute("errors", bindingResult.getFieldErrors());
            model.addAttribute("inputIncident", incident);
        } else {
            // Сохраняем новый инцедент
            long currentDateTime = new Date().getTime();
            incident.setTimestamp(new Timestamp(currentDateTime));
            incident.setUser(user);
            incidentRepo.save(incident);
            // Выводим список всех инцедентов
            List<Incident> incidents = incidentRepo.findByUser(user);
            model.addAttribute("incidents", incidents);
        }
        model.addAttribute("user", user);
        return "create";
    }
}
