package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.dto.TimelineEventDTO;
import com.tus.incidentmanagement.dto.UserDTO;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.TimelineEventEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.service.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@Tag(name = "Timeline", description = "Manage incident timeline events")
public class TimelineController {

    @Autowired
    private  TimelineService timelineService;

    /*public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }*/

    @Operation(summary = "Add timeline event to incident")
    @PostMapping("/{id}/timeline")
    public ResponseEntity<TimelineEventDTO> addTimelineEvent(
            @PathVariable Long id,
            @RequestBody TimelineEventDTO event) {

        TimelineEventEntity timelineEventEntity = new TimelineEventEntity();
        timelineEventEntity.setEventTime(event.getEventTime());
        timelineEventEntity.setDescription(event.getDescription());

        IncidentEntity incidentEntity = new IncidentEntity();
        if(event.getIncident() != null) {
            incidentEntity.setId(event.getIncident().getId());
            incidentEntity.setSeverity(event.getIncident().getSeverity());
            incidentEntity.setStatus(event.getIncident().getStatus());
            incidentEntity.setDescription(event.getIncident().getDescription());
            incidentEntity.setBlameless(event.getIncident().isBlameless());
            incidentEntity.setTitle(event.getIncident().getTitle());
            incidentEntity.setCreatedAt(event.getIncident().getCreatedAt());

            UserEntity userEntity = new UserEntity();
            if(event.getIncident().getCreatedBy() != null) {
                UserDTO userDTO = event.getIncident().getCreatedBy();
                userEntity.setUsername(userDTO.getUsername());
                userEntity.setRole(userDTO.getRole());
                userEntity.setPassword(userDTO.getPassword());
                incidentEntity.setCreatedBy(userEntity);
            }

        }
        timelineEventEntity.setIncident(incidentEntity);

        TimelineEventDTO saved = timelineService.addEvent(id, timelineEventEntity);

        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Get timeline for an incident")
    @GetMapping("/{id}/timeline")
    public List<TimelineEventDTO> getTimeline(@PathVariable Long id) {

        return timelineService.getTimeline(id);
    }
}
