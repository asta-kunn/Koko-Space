package id.ac.ui.cs.advprog.coworkingspace.workspace.controller;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.WorkspaceRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent.SpaceRentService;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.workspace.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/coworking-space/workspace")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final SpaceRentService spaceRentService;

    @GetMapping("/all")
    public ResponseEntity<List<Workspace>> getAllWorkspace() {
        List<Workspace> response = workspaceService.findAll();

        for (Workspace workspace: response) {
            spaceRentService.checkRentFinish(workspace.getId());
            updateAvailability(workspace);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable Integer id) {
        if (isInvalidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        Workspace response = workspaceService.findById(id);
        spaceRentService.checkRentFinish(response.getId());
        updateAvailability(response);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<Workspace> addWorkspace(@RequestBody WorkspaceRequest request) {
        if (isInvalidRequest(request)) {
            return ResponseEntity.badRequest().build();
        }

        Workspace response = workspaceService.create(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<Workspace> updateWorkspace(@PathVariable Integer id, @RequestBody WorkspaceRequest request) {
        if (isInvalidId(id) || isInvalidRequest(request)) {
            return ResponseEntity.badRequest().build();
        }

        Workspace response = workspaceService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<String> deleteWorkspace(@PathVariable Integer id) {
        if (isInvalidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        workspaceService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Workspace with id %d", id));
    }

    private void updateAvailability(Workspace workspace) {
        if (workspace.getFilledSeat() < workspace.getCapacity()) {
            workspace.setAvailability(true);
        }
    }

    private boolean isInvalidRequest(WorkspaceRequest request) {
        return (!Objects.equals(request.getType(), "COWORKING") &&
                !Objects.equals(request.getType(), "PERSONAL")) ||
                request.getCapacity() <= 0 || request.getHourlyPrice() <= 0 ||
                request.getDailyPrice() <= 0;
    }

    private boolean isInvalidId(Integer id) {
        return id < 0;
    }
}
