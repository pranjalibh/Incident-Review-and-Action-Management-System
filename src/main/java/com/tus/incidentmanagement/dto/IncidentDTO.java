package com.tus.incidentmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Incident details")
public class IncidentDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Database outage")
    private String title;

    @Schema(example = "Database is not responding")
    private String description;

    @Schema(example = "HIGH")
    private String severity;

    @Schema(example = "OPEN")
    private String status;

    @Schema(example = "false")
    private boolean blameless;

    @Schema(example = "2026-03-17T12:00:00")
    private LocalDateTime createdAt;

    @Schema(example = "UserDTO")
    private UserDTO createdBy;
}