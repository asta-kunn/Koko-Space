package id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.StatusType;
import lombok.*;
@lombok.Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AttendeeStatusList {

    private StatusType statusType;
    private String name;

    public static AttendeeStatusList fromAttendeeStatus(AttendeeStatus attendeeStatus) {
        return AttendeeStatusList.builder()
                .statusType(attendeeStatus.getStatusType())
                .name(attendeeStatus.getName())
                .build();
    }

}
