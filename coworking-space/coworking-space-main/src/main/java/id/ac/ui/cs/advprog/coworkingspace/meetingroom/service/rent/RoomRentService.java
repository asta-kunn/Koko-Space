package id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.rent;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentAdminResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentUserResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;

import java.util.List;

public interface RoomRentService {
    List<RoomRentAdminResponse> findAll();
    List<RoomRentUserResponse> findAllByUserId(Integer userId);
    RoomRent create(Integer userId, RoomRentRequest roomRentRequest);
    RoomRentAdminResponse findRoomRentById(Integer id);
    void delete(Integer id);

    RoomRent update(Integer userId, Integer id, RoomRentRequest roomRentRequest);
}
