package id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import lombok.*;
import java.util.Date;
import java.util.List;
@lombok.Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRentUserResponse {
    private Integer roomRentId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rentStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rentEnd;
    private Integer cost;
    private MeetingRoom meetingRoom;
    private Integer duration;

    private List<AttendeeStatusList> attendeeStatusList;

    public static RoomRentUserResponse fromRoomRent(RoomRent roomRent, List<AttendeeStatus> attendeeStatus) {
        return RoomRentUserResponse.builder()
                .roomRentId(roomRent.getId())
                .rentStart(roomRent.getRentStart())
                .rentEnd(roomRent.getRentEnd())
                .cost(roomRent.getCost())
                .meetingRoom(roomRent.getMeetingRoom())
                .duration(Math.toIntExact(roomRent.getDuration()))
                .attendeeStatusList(attendeeStatus
                        .stream()
                        .map(AttendeeStatusList::fromAttendeeStatus)
                        .toList())
                .build();
    }


}