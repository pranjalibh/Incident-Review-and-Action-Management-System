package com.tus.incidentmanagement.model;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getBlameless() {
        return blameless;
    }

    public void setBlameless(Boolean blameless) {
        this.blameless = blameless;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }
}
