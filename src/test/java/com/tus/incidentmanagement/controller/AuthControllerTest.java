package com.tus.incidentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.incidentmanagement.dto.LoginRequestDTO;
import com.tus.incidentmanagement.dto.LoginResponseDTO;
import com.tus.incidentmanagement.dto.UserDTO;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    //LOGIN SUCCESS
    @Test
    void login_shouldReturnToken() throws Exception {

        LoginRequestDTO request = buildLoginRequest();

        LoginResponseDTO response =
                new LoginResponseDTO("mockToken", "ADMIN");

        when(authService.login("john", "password"))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    //LOGIN INVALID REQUEST (validation)
    @Test
    void login_shouldReturnBadRequest_whenInvalidInput() throws Exception {

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    //GET USERS
    @Test
    void getUsers_shouldReturnList() throws Exception {

        UserDTO user1 = new UserDTO();
        user1.setUsername("john");
        user1.setRole("USER");

        UserDTO user2 = new UserDTO();
        user2.setUsername("mary");
        user2.setRole("ADMIN");

        when(authService.getUsers())
                .thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))   // 🔥 FIXED
                .andExpect(jsonPath("$[0].username").value("john"))
                .andExpect(jsonPath("$[1].username").value("mary"));
    }

    //CREATE USER
    @Test
    void createUser_shouldReturnUser() throws Exception {

        UserDTO user = new UserDTO();
        user.setUsername("john");
        user.setPassword("123");
        user.setRole("USER");

        when(authService.createUser(any()))
                .thenReturn(user);

        mockMvc.perform(post("/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    //FACTORY METHODS

    private LoginRequestDTO buildLoginRequest() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("john");
        request.setPassword("password");
        return request;
    }

}