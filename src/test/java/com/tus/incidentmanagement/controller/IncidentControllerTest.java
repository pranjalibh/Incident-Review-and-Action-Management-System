package com.tus.incidentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.model.Incident;
import com.tus.incidentmanagement.repository.UserRepository;
import com.tus.incidentmanagement.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllIncidents_shouldReturnList() throws Exception {

        when(incidentService.getAllIncidents())
                .thenReturn(Arrays.asList(buildIncident(), buildIncident()));

        mockMvc.perform(get("/api/admin/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void createIncident_shouldReturnIncident() throws Exception {

        Incident request = buildIncidentRequest();
        UserEntity user = buildUser("john", "MANAGER");
        IncidentEntity saved = buildIncident();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(incidentService.createIncident(any()))
                .thenReturn(saved);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null)
        );

        mockMvc.perform(post("/api/admin/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Incident"));
    }

    @Test
    void toggleBlameless_shouldWork_forManager() throws Exception {

        UserEntity user = buildUser("john", "MANAGER");
        IncidentEntity incident = buildIncident();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(incidentService.toggleBlameless(1L))
                .thenReturn(incident);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null)
        );

        mockMvc.perform(patch("/api/admin/incidents/1/blameless")
                        .with(user("john")))
                .andExpect(status().isOk());
    }

    @Test
    void toggleBlameless_shouldReturnBadRequest_ifNotManager() throws Exception {

        UserEntity user = buildUser("john", "USER");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null)
        );

        mockMvc.perform(patch("/api/admin/incidents/1/blameless"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getIncident_shouldReturnIncident() throws Exception {

        when(incidentService.getIncidentById(1L))
                .thenReturn(buildIncident());

        mockMvc.perform(get("/api/admin/incidents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Incident"));
    }

    @Test
    void closeIncident_shouldWork_forReviewer() throws Exception {

        UserEntity user = buildUser("john", "REVIEWER");
        IncidentEntity incident = buildIncident();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(incidentService.closeIncident(1L))
                .thenReturn(incident);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null)
        );

        mockMvc.perform(patch("/api/admin/incidents/1/close"))
                .andExpect(status().isOk());
    }

    @Test
    void closeIncident_shouldReturnBadRequest_ifNotReviewer() throws Exception {

        UserEntity user = buildUser("john", "USER");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(patch("/api/admin/incidents/1/close")
                        .with(user("john")))
                .andExpect(status().isBadRequest());
    }

    // helpers

    private Incident buildIncidentRequest() {
        return new Incident(
                null,
                "Test Incident",
                "Test Description",
                null,
                "HIGH",
                false,
                null,
                0L
        );
    }

    private IncidentEntity buildIncident() {
        IncidentEntity incident = new IncidentEntity();
        incident.setTitle("Test Incident");
        return incident;
    }

    private UserEntity buildUser(String username, String role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setRole(role);
        return user;
    }
}