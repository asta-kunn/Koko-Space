package id.ac.ui.cs.advprog.coworkingspace.meetingroom.exception;

public class MeetingRoomDoesNotExistException extends RuntimeException {
    public MeetingRoomDoesNotExistException(Integer id) {
        super("Meeting Room with id " + id + " does not exist");
    }
}

