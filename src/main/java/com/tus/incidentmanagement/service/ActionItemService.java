package com.tus.incidentmanagement.service;


import com.tus.incidentmanagement.dto.ActionItemDTO;
import com.tus.incidentmanagement.entity.ActionItemEntity;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.dao.ActionItemRepository;
import com.tus.incidentmanagement.dao.IncidentRepository;
import com.tus.incidentmanagement.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActionItemService {

    @Autowired
    private  ActionItemRepository actionItemRepository;
    @Autowired
    private  IncidentRepository incidentRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  IncidentService incidentService;
    @Autowired
    private AuthService authService;


    private boolean validateSla(IncidentEntity incident, LocalDateTime dueDate) {

        LocalDateTime now = incident.getCreatedAt();
        String severity=incident.getSeverity();

        switch(severity) {

            case "CRITICAL":
                return dueDate.isBefore(now.plusHours(24));

            case "HIGH":
                return dueDate.isBefore(now.plusHours(48));

            case "MEDIUM":
                return dueDate.isBefore(now.plusHours(120));

            default:
                return true;
        }

    }

    public ActionItemDTO createAction(Long incidentId,
                                         String description,
                                         String username,
                                         LocalDateTime dueDate) {

        IncidentEntity incident = incidentRepository.findIncidentById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if(!validateSla(incident, dueDate)) {
            throw new RuntimeException("SLA violation: due date exceeds allowed limit");
        }
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ActionItemEntity action = new ActionItemEntity();

        action.setDescription(description);
        action.setDueDate(dueDate);
        action.setIncident(incident);
        action.setAssignedTo(user);
        ActionItemEntity saved = actionItemRepository.save(action);
        return mapToDTO(saved);
    }

    public List<ActionItemDTO> getActions(Long id) {
        return actionItemRepository.findActionsByIncidentId(id)
                .stream()
                .map(this::mapToDTO)
                .toList();

    }
    @Transactional
    public ActionItemDTO completeAction(Long actionId) {
        actionItemRepository.completeAction(actionId);
        ActionItemEntity entity = actionItemRepository.findActionById(actionId);
        return mapToDTO(entity);
    }

    public List<ActionItemDTO> getMyActions(String username) {
        return actionItemRepository.findByAssignedUsername(username)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ActionItemDTO mapToDTO(ActionItemEntity entity) {

        ActionItemDTO dto = new ActionItemDTO();
        if(entity != null) {
            dto.setId(entity.getId());
            dto.setDescription(entity.getDescription());
            dto.setCompleted(entity.isCompleted());

            if (entity.getDueDate() != null) {
                dto.setDueDate(entity.getDueDate().toString());
            }

            if (entity.getAssignedTo() != null) {
                dto.setAssignedTo(authService.mapToDTO(entity.getAssignedTo()));
            }

            if (entity.getIncident() != null) {
                dto.setIncident(incidentService.mapToDTO(entity.getIncident()));
            }
        }

        return dto;
    }
}
