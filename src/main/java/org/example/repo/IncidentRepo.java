package org.example.repo;

import org.example.model.Incident;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepo extends JpaRepository<Incident, Long> {
    List<Incident> findByUser(User user);
}
