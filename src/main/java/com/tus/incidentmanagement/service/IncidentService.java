package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.dao.ActionItemRepository;
import com.tus.incidentmanagement.dao.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tus.incidentmanagement.dto.IncidentDTO;


import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private  IncidentRepository incidentRepository;
    @Autowired
    private  ActionItemRepository actionItemRepository;
    @Autowired
    private AuthService authService;



    public List<IncidentDTO> getAllIncidents() {
        List<IncidentEntity> listOfIncidents= (List<IncidentEntity>) incidentRepository.findAll();
        return listOfIncidents.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public IncidentDTO createIncident(IncidentEntity incident) {
        incident.setStatus("OPEN"); // required by user story
        return mapToDTO(incidentRepository.save(incident));
    }
    public IncidentDTO toggleBlameless(Long id) {

        IncidentEntity incident = getIncidentById(id);
        incident.setBlameless(!incident.isBlameless());

        return mapToDTO(incidentRepository.save(incident));
    }
    public IncidentEntity getIncidentById(Long id) {

        return incidentRepository.findIncidentById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
    public IncidentDTO closeIncident(Long id) {

        IncidentEntity incident = getIncidentById(id);

        long openActions = actionItemRepository.countOpenActions(id);

        if (openActions > 0) {
            throw new RuntimeException("Cannot close incident. All action items must be completed.");
        }

        incident.setStatus("CLOSED");

        return mapToDTO(incidentRepository.save(incident));
    }

    public IncidentDTO mapToDTO(IncidentEntity entity) {

        IncidentDTO dto = new IncidentDTO();
        if(entity != null) {
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setSeverity(entity.getSeverity());
            dto.setStatus(entity.getStatus());
            dto.setBlameless(entity.isBlameless());

            if (entity.getCreatedAt() != null) {
                dto.setCreatedAt(entity.getCreatedAt());
            }

            if (entity.getCreatedBy() != null) {
                dto.setCreatedBy(authService.mapToDTO(entity.getCreatedBy()));
            }
        }
        return dto;
    }
}