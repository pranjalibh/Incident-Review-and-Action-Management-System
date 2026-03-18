package com.tus.incidentmanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ActionRequest {

    private String description;

    private String assignedTo;

    private LocalDateTime dueDate;

}
