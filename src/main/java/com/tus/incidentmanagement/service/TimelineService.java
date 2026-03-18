package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.dto.TimelineEventDTO;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.TimelineEventEntity;
import com.tus.incidentmanagement.dao.IncidentRepository;
import com.tus.incidentmanagement.dao.TimelineEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimelineService {

    @Autowired
    private  TimelineEventRepository timelineRepository;
    @Autowired
    private  IncidentRepository incidentRepository;
    @Autowired
    private  IncidentService incidentService;


    public TimelineEventDTO addEvent(Long incidentId, TimelineEventEntity event) {

        IncidentEntity incident = incidentRepository.findIncidentById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        event.setIncident(incident);

        return mapToDTO(timelineRepository.save(event));
    }

    public List<TimelineEventDTO> getTimeline(Long incidentId) {
        List<TimelineEventEntity> listTimeline = timelineRepository.findTimelineByIncidentId(incidentId);
        return listTimeline.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TimelineEventDTO mapToDTO(TimelineEventEntity entity) {

        TimelineEventDTO dto = new TimelineEventDTO();
        if(entity != null) {
            dto.setId(entity.getId());
            dto.setDescription(entity.getDescription());

            if (entity.getEventTime() != null) {
                dto.setEventTime(entity.getEventTime());
            }

            if (entity.getIncident() != null) {
                dto.setIncident(incidentService.mapToDTO(entity.getIncident()));
            }
        }

        return dto;
    }
}
