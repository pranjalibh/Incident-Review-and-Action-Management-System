package com.tus.incidentmanagement.service;


import com.tus.incidentmanagement.entity.ActionItemEntity;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.repository.ActionItemRepository;
import com.tus.incidentmanagement.repository.IncidentRepository;
import com.tus.incidentmanagement.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActionItemService {

    private final ActionItemRepository actionItemRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public ActionItemService(ActionItemRepository actionItemRepository, IncidentRepository incidentRepository, UserRepository userRepository) {
        this.actionItemRepository = actionItemRepository;
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    private boolean validateSla(String severity, LocalDateTime dueDate) {

        LocalDateTime now = LocalDateTime.now();

        switch(severity) {

            case "CRITICAL":
                return dueDate.isBefore(now.plusHours(12));

            case "HIGH":
                return dueDate.isBefore(now.plusHours(24));

            case "MEDIUM":
                return dueDate.isBefore(now.plusHours(48));

            default:
                return true;
        }

    }

    public ActionItemEntity createAction(Long incidentId,
                                         String description,
                                         String username,
                                         LocalDateTime dueDate) {

        IncidentEntity incident = incidentRepository.findIncidentById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if(!validateSla(incident.getSeverity(), dueDate)) {
            throw new RuntimeException("SLA violation: due date exceeds allowed limit");
        }
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ActionItemEntity action = new ActionItemEntity();

        action.setDescription(description);
        action.setDueDate(dueDate);
        action.setIncident(incident);
        action.setAssignedTo(user);

        return actionItemRepository.save(action);
    }

    public List<ActionItemEntity> getActions(Long id) {
        return (List<ActionItemEntity>) actionItemRepository.findActionsByIncidentId(id);
    }
    @Transactional
    public ActionItemEntity completeAction(Long actionId) {
        actionItemRepository.completeAction(actionId);
        return actionItemRepository.findActionById(actionId);
    }
}
