package com.tus.incidentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.incidentmanagement.dto.ActionItemDTO;
import com.tus.incidentmanagement.entity.ActionItemEntity;
import com.tus.incidentmanagement.model.ActionRequest;
import com.tus.incidentmanagement.service.ActionItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActionItemService actionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAction_shouldReturnAction() throws Exception {

        ActionRequest request = buildActionRequest();
        ActionItemDTO action = buildAction();

        when(actionService.createAction(any(), any(), any(), any()))
                .thenReturn(action);

        mockMvc.perform(post("/api/incidents/1/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("fix issue"));
    }

    @Test
    void getActions_shouldReturnList() throws Exception {

        when(actionService.getActions(1L))
                .thenReturn(Arrays.asList(buildAction(), buildAction()));

        mockMvc.perform(get("/api/incidents/1/actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void completeAction_shouldReturnUpdatedAction() throws Exception {

        ActionItemDTO action = buildAction();

        when(actionService.completeAction(1L))
                .thenReturn(action);

        mockMvc.perform(put("/api/incidents/actions/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("fix issue"));
    }


    // helper methods

    private ActionRequest buildActionRequest() {
        ActionRequest req = new ActionRequest();
        req.setDescription("fix issue");
        req.setAssignedTo("john");
        req.setDueDate(LocalDateTime.now().plusHours(5));
        return req;
    }

    private ActionItemDTO buildAction() {
        ActionItemDTO action = new ActionItemDTO();
        action.setDescription("fix issue");
        return action;
    }
}