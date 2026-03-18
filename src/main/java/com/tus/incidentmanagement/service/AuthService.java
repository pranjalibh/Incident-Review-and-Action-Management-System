package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.config.JwtService;
import com.tus.incidentmanagement.dto.LoginResponseDTO;
import com.tus.incidentmanagement.dto.UserDTO;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.exception.InvalidCredentialsException;
import com.tus.incidentmanagement.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(String username, String password) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed for username: {}", username);
                    return new InvalidCredentialsException("Invalid credentials");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Invalid password attempt for username: {}", username);
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        logger.info("User {} logged in successfully", username);
        return new LoginResponseDTO(token, user.getRole());
    }

    public List<UserDTO> getUsers() {

       List<UserEntity> listOfUsers= userRepository.findAll();
       return listOfUsers.stream().map(this::mapToDTO)
                .toList();
    }

    public UserDTO createUser(UserEntity user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapToDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO mapToDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        if(user != null) {

            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());

            if (user.getCreatedAt() != null) {
                dto.setCreatedAt(user.getCreatedAt().toString());
            }
        }
        return dto;
    }
}