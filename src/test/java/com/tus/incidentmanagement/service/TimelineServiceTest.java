package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.dto.TimelineEventDTO;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.TimelineEventEntity;
import com.tus.incidentmanagement.dao.IncidentRepository;
import com.tus.incidentmanagement.dao.TimelineEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimelineServiceTest {

    @Mock
    private TimelineEventRepository timelineRepository;

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private TimelineService timelineService;

    @Mock
    private IncidentService incidentService;

    @Test
    void addEvent_shouldAttachIncidentAndSave() {
        IncidentEntity incident = new IncidentEntity();
        incident.setId(1L);

        TimelineEventEntity event = new TimelineEventEntity();

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        when(timelineRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        TimelineEventDTO result = timelineService.addEvent(1L, event);

        assertNotNull(result);
        verify(timelineRepository).save(event);
    }

    @Test
    void addEvent_shouldThrow_whenIncidentNotFound() {
        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.empty());

        TimelineEventEntity event = new TimelineEventEntity();

        assertThrows(RuntimeException.class,
                () -> timelineService.addEvent(1L, event));
    }

    @Test
    void getTimeline_shouldReturnEvents() {
        when(timelineRepository.findTimelineByIncidentId(1L))
                .thenReturn(Arrays.asList(new TimelineEventEntity(), new TimelineEventEntity()));

        var result = timelineService.getTimeline(1L);

        assertEquals(2, result.size());
    }
}