package id.ac.ui.cs.advprog.coworkingspace.meetingroom.controller;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentAdminResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentUserResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.rent.RoomRentService;
import id.ac.ui.cs.advprog.coworkingspace.auth.model.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/coworking-space/room-rent")
@CrossOrigin(origins="*", allowedHeaders = "*")
@RequiredArgsConstructor
public class RoomRentController {
    @Autowired
    private final RoomRentService roomRentService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<List<RoomRentAdminResponse>> getAllRoomRent() {
        List<RoomRentAdminResponse> response = roomRentService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<List<RoomRentUserResponse>> getAllUserRoomRent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((JwtPayload) authentication.getCredentials()).getUserId();
        List<RoomRentUserResponse> response;
        response = roomRentService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA') || hasAuthority('PELANGGAN')")
    public ResponseEntity<RoomRentAdminResponse> getRoomRentById(@PathVariable Integer id) {
        RoomRentAdminResponse response;
        response = roomRentService.findRoomRentById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<RoomRent> createRoomRent(@RequestBody RoomRentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((JwtPayload) authentication.getCredentials()).getUserId();
        RoomRent response;
        response = roomRentService.create(userId, request);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<RoomRent> updateRoomRent(@PathVariable Integer id, @RequestBody RoomRentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((JwtPayload) authentication.getCredentials()).getUserId();
        RoomRent response;
        response = roomRentService.update(userId, id, request);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<String> deleteRoomRent(@PathVariable Integer id) {
        roomRentService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Room Rent with id %d", id));
    }

}
