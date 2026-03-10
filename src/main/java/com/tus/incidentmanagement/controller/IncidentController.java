package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.model.Incident;
import com.tus.incidentmanagement.repository.UserRepository;
import com.tus.incidentmanagement.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final UserRepository userRepository;

    public IncidentController(IncidentService incidentService, UserRepository userRepository) {
        this.incidentService = incidentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<IncidentEntity> getAllIncidents() {
        return incidentService.getAllIncidents();
    }
    @PostMapping
    public ResponseEntity<IncidentEntity> createIncident(@Valid @RequestBody Incident request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        IncidentEntity incident = new IncidentEntity();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setSeverity(request.getSeverity());
        incident.setBlameless(request.getBlameless());
        incident.setCreatedBy(user);


        IncidentEntity savedIncident = incidentService.createIncident(incident);

        return ResponseEntity.ok(savedIncident);
    }
    @PatchMapping("/{id}/blameless")
    public ResponseEntity<IncidentEntity> toggleBlameless(@PathVariable Long id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!user.getRole().equals("MANAGER")) {
            return ResponseEntity.badRequest().build();
        }

        IncidentEntity incident = incidentService.toggleBlameless(id);

        return ResponseEntity.ok(incident);
    }
    @GetMapping("/{id}")
    public ResponseEntity<IncidentEntity> getIncident(@PathVariable Long id) {

        IncidentEntity incident = incidentService.getIncidentById(id);

        return ResponseEntity.ok(incident);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<?> closeIncident(@PathVariable Long id) {

        try {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();

            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if(!user.getRole().equals("REVIEWER")) {
                return ResponseEntity.badRequest().body("You are not a reviewer");
            }

            IncidentEntity incident = incidentService.closeIncident(id);

            return ResponseEntity.ok(incident);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

}