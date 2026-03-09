package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.entity.TimelineEventEntity;
import com.tus.incidentmanagement.service.TimelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class TimelineController {

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @PostMapping("/{id}/timeline")
    public ResponseEntity<TimelineEventEntity> addTimelineEvent(
            @PathVariable Long id,
            @RequestBody TimelineEventEntity event) {

        TimelineEventEntity saved = timelineService.addEvent(id, event);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/timeline")
    public List<TimelineEventEntity> getTimeline(@PathVariable Long id) {

        return timelineService.getTimeline(id);
    }
}
