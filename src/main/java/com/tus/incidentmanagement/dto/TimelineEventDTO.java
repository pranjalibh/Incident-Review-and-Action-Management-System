package com.tus.incidentmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Timeline event for an incident")
public class TimelineEventDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Incident detected")
    private String description;

    @Schema(example = "2026-03-17T10:15:00")
    private LocalDateTime eventTime;

    @Schema(example = "IncidentDTO")
    private IncidentDTO incident;

}