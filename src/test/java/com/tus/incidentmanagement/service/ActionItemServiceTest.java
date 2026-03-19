package com.tus.incidentmanagement.service;

import com.tus.incidentmanagement.dto.ActionItemDTO;
import com.tus.incidentmanagement.dto.IncidentDTO;
import com.tus.incidentmanagement.dto.UserDTO;
import com.tus.incidentmanagement.entity.ActionItemEntity;
import com.tus.incidentmanagement.entity.IncidentEntity;
import com.tus.incidentmanagement.entity.UserEntity;
import com.tus.incidentmanagement.dao.ActionItemRepository;
import com.tus.incidentmanagement.dao.IncidentRepository;
import com.tus.incidentmanagement.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionItemServiceTest {

    @Mock
    private ActionItemRepository actionItemRepository;

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActionItemService actionItemService;

    @Mock
    private IncidentService incidentService;

    @Mock
    private AuthService authService;

    @Test
    void createAction_shouldWork_whenValidInput() {
        IncidentEntity incident = new IncidentEntity();
        incident.setSeverity("HIGH");
        incident.setCreatedAt(LocalDateTime.now());

        UserEntity user = new UserEntity();
        user.setUsername("john");

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(actionItemRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("john");

        when(authService.mapToDTO(any()))
                .thenReturn(userDTO);

        when(incidentService.mapToDTO(any()))
                .thenReturn(new IncidentDTO());

        ActionItemDTO result = actionItemService.createAction(
                1L,
                "fix issue",
                "john",
                LocalDateTime.now().plusHours(10)
        );

        assertNotNull(result);
        assertEquals("fix issue", result.getDescription());
        assertEquals(userDTO, result.getAssignedTo());
    }

    @Test
    void createAction_shouldThrow_whenIncidentNotFound() {
        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                actionItemService.createAction(1L, "desc", "john", LocalDateTime.now()));
    }

    @Test
    void createAction_shouldThrow_whenUserNotFound() {
        IncidentEntity incident = new IncidentEntity();
        incident.setSeverity("LOW");
        incident.setCreatedAt(LocalDateTime.now());

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                actionItemService.createAction(1L, "desc", "john", LocalDateTime.now()));
    }

    @Test
    void createAction_shouldThrow_whenSlaViolated() {
        IncidentEntity incident = new IncidentEntity();
        incident.setSeverity("CRITICAL");
        incident.setCreatedAt(LocalDateTime.now());

        when(incidentRepository.findIncidentById(1L))
                .thenReturn(Optional.of(incident));

        // due date too far → violates SLA
        LocalDateTime badDueDate = LocalDateTime.now().plusDays(2);

        assertThrows(RuntimeException.class, () ->
                actionItemService.createAction(1L, "desc", "john", badDueDate));
    }

    @Test
    void getActions_shouldReturnList() {
        when(actionItemRepository.findActionsByIncidentId(1L))
                .thenReturn(Arrays.asList(new ActionItemEntity(), new ActionItemEntity()));

        var result = actionItemService.getActions(1L);

        assertEquals(2, result.size());
    }

    @Test
    void completeAction_shouldCallUpdateAndReturnEntity() {
        ActionItemEntity action = new ActionItemEntity();

        doNothing().when(actionItemRepository).completeAction(1L);
        when(actionItemRepository.findActionById(1L))
                .thenReturn(action);

        ActionItemDTO result = actionItemService.completeAction(1L);

        assertNotNull(result);
        verify(actionItemRepository).completeAction(1L);
        verify(actionItemRepository).findActionById(1L);
    }

    @Test
    void getMyActions_shouldReturnAssignedActions() {
        when(actionItemRepository.findByAssignedUsername("john"))
                .thenReturn(Arrays.asList(new ActionItemEntity()));

        var result = actionItemService.getMyActions("john");

        assertEquals(1, result.size());
    }
}