package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.repository.ActionItemRepository;
import com.tus.incidentmanagement.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final ActionItemRepository actionItemRepository;

    public IncidentService(IncidentRepository incidentRepository,
                           ActionItemRepository actionItemRepository) {
        this.incidentRepository = incidentRepository;
        this.actionItemRepository = actionItemRepository;
    }


    public List<IncidentEntity> getAllIncidents() {
        return (List<IncidentEntity>) incidentRepository.findAll();
    }

    public IncidentEntity createIncident(IncidentEntity incident) {
        incident.setStatus("OPEN"); // required by user story
        return incidentRepository.save(incident);
    }
    public IncidentEntity toggleBlameless(Long id) {

        IncidentEntity incident = getIncidentById(id);
        incident.setBlameless(!incident.isBlameless());

        return incidentRepository.save(incident);
    }
    public IncidentEntity getIncidentById(Long id) {

        return incidentRepository.findIncidentById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
    public IncidentEntity closeIncident(Long id) {

        IncidentEntity incident = getIncidentById(id);

        long openActions = actionItemRepository.countOpenActions(id);

        if (openActions > 0) {
            throw new RuntimeException("Cannot close incident. All action items must be completed.");
        }

        incident.setStatus("CLOSED");

        return incidentRepository.save(incident);
    }
}