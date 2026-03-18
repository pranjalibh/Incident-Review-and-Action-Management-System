package com.tus.incidentmanagement.dto;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Action item assigned to a user for an incident")
public class ActionItemDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Restart the server")
    private String description;

    @Schema(example = "2026-03-20T10:00:00")
    private String dueDate;

    @Schema(example = "false")
    private boolean completed;

    @Schema(example = "UserDTO")
    private UserDTO assignedTo;

    @Schema(example = "IncidentDTO")
    private IncidentDTO incident;


}