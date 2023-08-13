package id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Integer> {
    @NonNull
    List<MeetingRoom> findAll();
    @NonNull
    MeetingRoom findMeetingRoomById(@NonNull Integer id);
    void deleteById(@NonNull Integer id);
}
