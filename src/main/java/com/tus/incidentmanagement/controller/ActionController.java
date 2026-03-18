package com.tus.incidentmanagement.controller;

import com.tus.incidentmanagement.dto.ActionItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import com.tus.incidentmanagement.model.ActionRequest;
import com.tus.incidentmanagement.service.ActionItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@Tag(name = "Actions", description = "Manage action items for incidents")
public class ActionController {

    @Autowired
    private ActionItemService actionService;

   /* public ActionController(ActionItemService actionService) {
        this.actionService = actionService;
    }*/

    @Operation(summary = "Create an action item for an incident")
    @PostMapping("/{id}/actions")
    public ResponseEntity<ActionItemDTO> createAction(
            @PathVariable Long id,
            @RequestBody ActionRequest request) {

        ActionItemDTO action =
                actionService.createAction(
                        id,
                        request.getDescription(),
                        request.getAssignedTo(),
                        request.getDueDate());

        return ResponseEntity.ok(action);
    }

    @Operation(summary = "Get all action items for an incident")
    @GetMapping("/{id}/actions")
    public List<ActionItemDTO> getActions(@PathVariable Long id) {
        return actionService.getActions(id);
    }

    @Operation(summary = "Mark an action item as complete")
    @PutMapping("/actions/{actionId}/complete")
    public ResponseEntity<ActionItemDTO> completeAction(@PathVariable Long actionId) {

        ActionItemDTO action = actionService.completeAction(actionId);

        return ResponseEntity.ok(action);
    }

    @Operation(summary = "Get actions assigned to the logged-in user")
    @GetMapping("/my/actions")
    public List<ActionItemDTO> getMyActions() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return actionService.getMyActions(username);
    }

}
