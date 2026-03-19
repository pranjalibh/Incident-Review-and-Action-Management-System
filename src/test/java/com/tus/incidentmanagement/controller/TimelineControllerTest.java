package com.tus.incidentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.incidentmanagement.dto.TimelineEventDTO;
import com.tus.incidentmanagement.entity.TimelineEventEntity;
import com.tus.incidentmanagement.service.TimelineService;
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
class TimelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimelineService timelineService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addTimelineEvent_shouldReturnEvent() throws Exception {

        TimelineEventDTO event = buildEventDto();

        when(timelineService.addEvent(any(), any()))
                .thenReturn(event);

        mockMvc.perform(post("/api/incidents/1/timeline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("event happened"));
    }

    @Test
    void getTimeline_shouldReturnList() throws Exception {

        when(timelineService.getTimeline(1L))
                .thenReturn(Arrays.asList(buildEventDto(), buildEventDto()));

        mockMvc.perform(get("/api/incidents/1/timeline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // helper
    private TimelineEventDTO buildEventDto() {
        TimelineEventDTO event = new TimelineEventDTO();
        event.setDescription("event happened");
        return event;
    }
}