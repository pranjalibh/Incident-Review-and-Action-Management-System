package com.tus.incidentmanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Incident {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String severity;
    private Boolean blameless;
    private LocalDateTime created;
    private long createdBy;

    public Incident(Long id, String title, String description, String status, String severity, Boolean blameless, LocalDateTime created, long createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.severity = severity;
        this.blameless = blameless;
        this.created = created;
        this.createdBy = createdBy;
    }

}
