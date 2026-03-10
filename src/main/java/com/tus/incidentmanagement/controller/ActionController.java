package com.tus.incidentmanagement.controller;

import org.springframework.security.core.Authentication;
import com.tus.incidentmanagement.entity.ActionItemEntity;
import com.tus.incidentmanagement.model.ActionRequest;
import com.tus.incidentmanagement.service.ActionItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class ActionController {

    private final ActionItemService actionService;

    public ActionController(ActionItemService actionService) {
        this.actionService = actionService;
    }

    @PostMapping("/{id}/actions")
    public ResponseEntity<ActionItemEntity> createAction(
            @PathVariable Long id,
            @RequestBody ActionRequest request) {

        ActionItemEntity action =
                actionService.createAction(
                        id,
                        request.getDescription(),
                        request.getAssignedTo(),
                        request.getDueDate());

        return ResponseEntity.ok(action);
    }

    @GetMapping("/{id}/actions")
    public List<ActionItemEntity> getActions(@PathVariable Long id) {
        return actionService.getActions(id);
    }

    @PutMapping("/actions/{actionId}/complete")
    public ResponseEntity<ActionItemEntity> completeAction(@PathVariable Long actionId) {

        ActionItemEntity action = actionService.completeAction(actionId);

        return ResponseEntity.ok(action);
    }

    @GetMapping("/my/actions")
    public List<ActionItemEntity> getMyActions() {
        System.out.println("Inside get user action item controller");
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("user name" + username);
        return actionService.getMyActions(username);
    }

}
