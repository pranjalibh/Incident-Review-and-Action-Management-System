package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.config.JwtService;
import com.tus.incidentmanagement.dto.LoginResponseDTO;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.exception.InvalidCredentialsException;
import com.tus.incidentmanagement.repository.UserRepository;
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

    public List<UserEntity> getUsers() {
       return userRepository.findAll();
    }
}