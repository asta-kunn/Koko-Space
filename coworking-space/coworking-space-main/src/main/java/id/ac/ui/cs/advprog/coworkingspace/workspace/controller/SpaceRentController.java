package id.ac.ui.cs.advprog.coworkingspace.workspace.controller;

import id.ac.ui.cs.advprog.coworkingspace.auth.model.JwtPayload;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentResponse;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent.SpaceRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/coworking-space/space-rent")
@RequiredArgsConstructor
public class SpaceRentController {
    private final SpaceRentService spaceRentService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<List<SpaceRentResponse>> getAllSpaceRent() {
        List<SpaceRentResponse> response = spaceRentService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<List<SpaceRentResponse>> getAllUserSpaceRent() {
        List<SpaceRentResponse> response = spaceRentService.findAllByUserId(getCurrentUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA') || hasAuthority('PELANGGAN')")
    public ResponseEntity<SpaceRentResponse> getSpaceRentById(@PathVariable Integer id) {
        if (isInvalidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        SpaceRentResponse response = spaceRentService.getSpaceRentById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<SpaceRent> createSpaceRent(@RequestBody SpaceRentRequest request) {
        if (isInvalidRequest(request)) {
            return ResponseEntity.badRequest().build();
        }
        SpaceRent response = spaceRentService.create(getCurrentUserId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<String> deleteSpaceRent(@PathVariable Integer id) throws IllegalArgumentException {
        if (isInvalidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        spaceRentService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Space Rent with id %d", id));
    }

    @PutMapping("/extend/{id}")
    public ResponseEntity<String> extendSpaceRent(@PathVariable Integer id, @RequestBody SpaceRentRequest request) throws IllegalArgumentException {
        if (isInvalidId(id) || isInvalidRequest(request)) {
            return ResponseEntity.badRequest().build();
        }
        spaceRentService.extendRent(id, request.getDuration());
        return ResponseEntity.ok(String.format("Extended Space Rent with id %d", id));
    }

    @PutMapping("/upgrade/{id}")
    public ResponseEntity<SpaceRent> upgradeSpaceRent(@PathVariable Integer id, @RequestBody SpaceRentRequest request) throws IllegalArgumentException {
        if (isInvalidId(id) || isInvalidRequest(request)) {
            return ResponseEntity.badRequest().build();
        }
        SpaceRent response = spaceRentService.upgrade(id, request.getWorkspaceId());
        return ResponseEntity.ok(response);
    }

    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtPayload) authentication.getCredentials()).getUserId();
    }

    private boolean isInvalidRequest(SpaceRentRequest request) {
        return request.getDuration() < 0 || request.getWorkspaceId() < 0;
    }

    private boolean isInvalidId(Integer id) {
        return id < 0;
    }
}
