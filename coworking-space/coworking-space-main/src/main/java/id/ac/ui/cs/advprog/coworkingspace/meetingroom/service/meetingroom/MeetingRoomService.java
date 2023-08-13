package id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.meetingroom;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.MeetingRoomRequest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface  MeetingRoomService {
    List<MeetingRoom> findAll();
    MeetingRoom findMeetingRoomById(Integer id);
    MeetingRoom create(MeetingRoomRequest request);
    MeetingRoom update(Integer id, MeetingRoomRequest request);
    void delete(Integer id);
}

