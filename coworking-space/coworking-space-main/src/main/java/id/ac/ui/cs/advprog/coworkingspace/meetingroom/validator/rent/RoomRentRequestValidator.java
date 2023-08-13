package id.ac.ui.cs.advprog.coworkingspace.meetingroom.validator.rent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentRequest;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
@Component
public class RoomRentRequestValidator {

    private RoomRentRequestValidator(){

    }
    public static boolean validate(RoomRentRequest request, int capacity, boolean availability) {
        if(!availability) {
            return false;
        }
        Date rentStart = request.getRentStart();
        Date rentEnd = request.getRentEnd();
        int duration = request.getDuration();
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(rentStart);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(rentEnd);

        long diffMillis = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
        int diffDays = (int) (diffMillis / (24 * 60 * 60 * 1000));

        int calculatedDuration = diffDays + 1;
        if(calculatedDuration != duration) {
            return false;
        }
        return request.getAttendeeStatusList().size() <= capacity;
    }
}
