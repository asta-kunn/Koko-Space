package id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;
@lombok.Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRentRequest {
    private Integer meetingRoomId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rentStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rentEnd;
    private Integer duration;
    private Integer cost;

    private List<AttendeeStatusList> attendeeStatusList;
}
