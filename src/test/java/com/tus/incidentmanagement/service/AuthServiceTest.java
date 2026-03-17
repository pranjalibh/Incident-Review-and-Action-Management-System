package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.config.JwtService;
import com.tus.incidentmanagement.dto.LoginResponseDTO;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.exception.InvalidCredentialsException;
import com.tus.incidentmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    // SUCCESS LOGIN
    @Test
    void LoginSuccessfully() {
        UserEntity user = new UserEntity();
        user.setUsername("john");
        user.setPassword("encodedPassword");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken("john", "ADMIN"))
                .thenReturn("mockToken");

        LoginResponseDTO response = authService.login("john", "password");

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        assertEquals("ADMIN", response.getRole());
    }

    //USER NOT FOUND
    @Test
    void ThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login("john", "password"));
    }

    //WRONG PASSWORD
    @Test
    void ThrowExceptionWhenPasswordIsIncorrect() {
        UserEntity user = new UserEntity();
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login("john", "wrongPassword"));
    }

    //VERIFY TOKEN GENERATION
    @Test
    void CallJwtServiceWhenLoginSuccessful() {
        UserEntity user = new UserEntity();
        user.setUsername("john");
        user.setPassword("encodedPassword");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);

        when(jwtService.generateToken(any(), any()))
                .thenReturn("token");

        authService.login("john", "password");

        verify(jwtService, times(1))
                .generateToken("john", "ADMIN");
    }

    //GET USERS
    @Test
    void ReturnAllUsers() {
        when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity(), new UserEntity()));

        assertEquals(2, authService.getUsers().size());
    }

    //CREATE USER (PASSWORD ENCODED)
    @Test
    void EncodePasswordAndSaveUser() {
        UserEntity user = new UserEntity();
        user.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(user);

        UserEntity savedUser = authService.createUser(user);

        assertNotNull(savedUser);
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(user);
    }
}