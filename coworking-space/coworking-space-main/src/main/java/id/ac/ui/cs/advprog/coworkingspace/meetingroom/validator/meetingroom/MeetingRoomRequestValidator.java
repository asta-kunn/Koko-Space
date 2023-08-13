package id.ac.ui.cs.advprog.coworkingspace.meetingroom.validator.meetingroom;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.MeetingRoomRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoomType;
import org.springframework.stereotype.Component;

@Component
public class MeetingRoomRequestValidator {

    private MeetingRoomRequestValidator() {

    }
    public static boolean validate(MeetingRoomRequest request) {
        boolean exist = false;
        for (MeetingRoomType value : MeetingRoomType.values()) {
            if (value.name().equals(request.getType())) {
                exist = true;
                break;
            }
        }
        if(!exist) {
            return false;
        }
        return !(request.getCapacity() <= 0 || request.getPrice() <= 0);
    }
}
