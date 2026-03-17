package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.repository.ActionItemRepository;
import com.tus.incidentmanagement.repository.IncidentRepository;
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
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private ActionItemRepository actionItemRepository;

    @InjectMocks
    private IncidentService incidentService;

    @Test
    void getAllIncidents_shouldReturnList() {
        when(incidentRepository.findAll())
                .thenReturn(Arrays.asList(new IncidentEntity(), new IncidentEntity()));

        var result = incidentService.getAllIncidents();

        assertEquals(2, result.size());
    }

    @Test
    void createIncident_shouldSetStatusToOpen() {
        IncidentEntity incident = new IncidentEntity();

        when(incidentRepository.save(any()))
                .thenReturn(incident);

        IncidentEntity saved = incidentService.createIncident(incident);

        assertEquals("OPEN", incident.getStatus());
        verify(incidentRepository).save(incident);
    }

    @Test
    void toggleBlameless_shouldFlipValue() {
        IncidentEntity incident = new IncidentEntity();
        incident.setId(1L);
        incident.setBlameless(false);

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        when(incidentRepository.save(any()))
                .thenReturn(incident);

        IncidentEntity updated = incidentService.toggleBlameless(1L);

        assertTrue(updated.isBlameless());
        verify(incidentRepository).save(incident);
    }

    @Test
    void getIncidentById_shouldReturnIncident() {
        IncidentEntity incident = new IncidentEntity();
        incident.setId(1L);

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        IncidentEntity result = incidentService.getIncidentById(1L);

        assertNotNull(result);
    }

    @Test
    void getIncidentById_shouldThrowWhenNotFound() {
        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> incidentService.getIncidentById(1L));
    }

    @Test
    void closeIncident_shouldCloseWhenNoOpenActions() {
        IncidentEntity incident = new IncidentEntity();
        incident.setId(1L);

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        when(actionItemRepository.countOpenActions(1L))
                .thenReturn(0L);

        when(incidentRepository.save(any()))
                .thenReturn(incident);

        IncidentEntity result = incidentService.closeIncident(1L);

        assertEquals("CLOSED", result.getStatus());
        verify(incidentRepository).save(incident);
    }

    @Test
    void closeIncident_shouldThrowIfOpenActionsExist() {
        IncidentEntity incident = new IncidentEntity();
        incident.setId(1L);

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        when(actionItemRepository.countOpenActions(1L))
                .thenReturn(2L);

        assertThrows(RuntimeException.class,
                () -> incidentService.closeIncident(1L));

        verify(incidentRepository, never()).save(any());
    }
}