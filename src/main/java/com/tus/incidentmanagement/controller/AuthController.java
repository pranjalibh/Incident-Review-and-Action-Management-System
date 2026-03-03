package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.dto.LoginRequestDTO;
import com.tus.incidentmanagement.dto.LoginResponseDTO;
import com.tus.incidentmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response =
                authService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}