package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.dto.LoginRequestDTO;
import com.tus.incidentmanagement.dto.LoginResponseDTO;
import com.tus.incidentmanagement.dto.UserDTO;
import com.tus.incidentmanagement.entity.ActionItemEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and management")
public class AuthController {

    @Autowired
    private  AuthService authService;

    /*public AuthController(AuthService authService) {
        this.authService = authService;
    }*/

    @Operation(summary = "Login user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response =
                authService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return authService.getUsers();
    }

    @Operation(summary = "Create a new user")
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        UserEntity userEntity= MapToEntity(user);
        UserDTO createdUser = authService.createUser(userEntity);
        return ResponseEntity.ok(createdUser);
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    private UserEntity MapToEntity(UserDTO user) {
        UserEntity createdUser = new UserEntity();
        createdUser.setUsername(user.getUsername());
        createdUser.setPassword(user.getPassword());
        createdUser.setRole(user.getRole());
        return createdUser;
    }
}