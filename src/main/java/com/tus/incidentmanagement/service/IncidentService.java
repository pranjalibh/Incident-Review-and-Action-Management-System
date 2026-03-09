package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public List<IncidentEntity> getAllIncidents() {
        return (List<IncidentEntity>) incidentRepository.findAll();
    }
}