package id.ac.ui.cs.advprog.coworkingspace.meetingroom.controller;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.MeetingRoomRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.meetingroom.MeetingRoomService;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/coworking-space/meetingroom")
@RequiredArgsConstructor
public class MeetingRoomController {
    private final MeetingRoomService meetingRoomService;

    @GetMapping("/all")
    public ResponseEntity<List<MeetingRoom>> getAllMeetingRoom() {
        List<MeetingRoom> response = meetingRoomService.findAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MeetingRoom> getMeetingRoomById(@PathVariable Integer id) {
        MeetingRoom response = meetingRoomService.findMeetingRoomById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<MeetingRoom> addMeetingRoom(@RequestBody MeetingRoomRequest request) {
        MeetingRoom response = meetingRoomService.create(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<MeetingRoom> updateMeetingRoom(@PathVariable Integer id, @RequestBody MeetingRoomRequest request) {
        MeetingRoom response = meetingRoomService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<String> deleteMeetingRoom(@PathVariable Integer id) {
        meetingRoomService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Meeting Room with id %d", id));
    }
}
