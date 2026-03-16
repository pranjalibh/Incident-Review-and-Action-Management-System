package com.tus.incidentmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "timeline_events")
public class TimelineEventEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private IncidentEntity incident;

    public Long getId() { return id; }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventTime() { return eventTime; }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public IncidentEntity getIncident() { return incident; }

    public void setIncident(IncidentEntity incident) {
        this.incident = incident;
    }
}
