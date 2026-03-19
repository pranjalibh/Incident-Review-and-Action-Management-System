package com.tus.incidentmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {

    private String token;
    private String role;

    public LoginResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }

}