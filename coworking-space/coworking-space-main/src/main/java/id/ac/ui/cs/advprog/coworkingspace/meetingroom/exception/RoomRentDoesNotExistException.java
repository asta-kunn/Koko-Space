package id.ac.ui.cs.advprog.coworkingspace.meetingroom.exceptions;
public class RoomRentDoesNotExistException extends RuntimeException {
    public RoomRentDoesNotExistException(Integer id) {
        super("Room rent with id " + id + " does not exist");
    }
}

