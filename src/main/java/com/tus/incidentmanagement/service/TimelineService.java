package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.TimelineEventEntity;
import com.tus.incidentmanagement.repository.IncidentRepository;
import com.tus.incidentmanagement.repository.TimelineEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimelineService {

    private final TimelineEventRepository timelineRepository;
    private final IncidentRepository incidentRepository;

    public TimelineService(TimelineEventRepository timelineRepository,
                           IncidentRepository incidentRepository) {
        this.timelineRepository = timelineRepository;
        this.incidentRepository = incidentRepository;
    }

    public TimelineEventEntity addEvent(Long incidentId, TimelineEventEntity event) {

        IncidentEntity incident = incidentRepository.findIncidentById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        event.setIncident(incident);

        return timelineRepository.save(event);
    }

    public List<TimelineEventEntity> getTimeline(Long incidentId) {
        return timelineRepository.findTimelineByIncidentId(incidentId);
    }
}
