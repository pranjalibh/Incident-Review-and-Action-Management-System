package com.tus.incidentmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User information (safe, no password)")
public class UserDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "john")
    private String username;

    @Schema(example = "MANAGER")
    private String role;

    @Schema(example = "2026-03-17T09:00:00")
    private String createdAt;

    @Schema(example = "admin123")
    private String password;


}