package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.service.IncidentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<IncidentEntity> getAllIncidents() {
        return incidentService.getAllIncidents();
    }
}