package id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.rent;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentAdminResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentUserResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.exceptions.RoomRentDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.exception.MeetingRoomDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.RoomRentRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.AttendeeStatusRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.MeetingRoomRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.validator.rent.RoomRentRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomRentServiceImpl implements RoomRentService {
    private final RoomRentRepository roomRentRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final AttendeeStatusRepository attendeeStatusRepository;
    @Override
    public List<RoomRentAdminResponse> findAll() {
        return roomRentRepository.findAll()
                .stream()
                .map(roomrent -> RoomRentAdminResponse.fromRoomRent(roomrent, attendeeStatusRepository.findAllByRentId(roomrent.getId())))
                .toList();
    }

    @Override
    public List<RoomRentUserResponse> findAllByUserId(Integer userId) {
        return roomRentRepository.findAllByUserId(userId)
                .stream()
                .map(roomrent -> RoomRentUserResponse.fromRoomRent(roomrent, attendeeStatusRepository.findAllByRentId(roomrent.getId())))
                .toList();
    }

    @Override
    public RoomRent create(Integer userId, RoomRentRequest roomRentRequest) {
        if(isMeetingRoomDoesNotExist(roomRentRequest.getMeetingRoomId())){
            throw new MeetingRoomDoesNotExistException(roomRentRequest.getMeetingRoomId());
        }
        var meetingroom = meetingRoomRepository.findMeetingRoomById(roomRentRequest.getMeetingRoomId());
        if(!RoomRentRequestValidator.validate(roomRentRequest, meetingroom.getCapacity(),meetingroom.getAvailability())){
            throw new IllegalArgumentException("Invalid room rent request");
        }
        var roomRent = RoomRent.builder().userId(userId)
            .rentStart(roomRentRequest.getRentStart())
            .rentEnd(roomRentRequest.getRentEnd())
            .meetingRoom(meetingroom)
            .duration(roomRentRequest.getDuration())
            .cost(meetingroom.calculateCost(roomRentRequest.getDuration()))
            .build();
        roomRentRepository.save(roomRent);
        roomRentRequest.getAttendeeStatusList().forEach(details -> {
            attendeeStatusRepository.save(
                    AttendeeStatus.builder()
                            .rentId(roomRent.getId())
                            .name(details.getName())
                            .statusType(details.getStatusType())
                            .build()
            );
        });
        return roomRent;
    }

    @Override
    public RoomRent update(Integer userId, Integer id, RoomRentRequest roomRentRequest) {
        if (isRoomRentDoesNotExist(id)) {
            throw new RoomRentDoesNotExistException(id);
        }
        if(isMeetingRoomDoesNotExist(roomRentRequest.getMeetingRoomId())){
            throw new MeetingRoomDoesNotExistException(roomRentRequest.getMeetingRoomId());
        }
        var meetingroom = meetingRoomRepository.findMeetingRoomById(roomRentRequest.getMeetingRoomId());
        if(!RoomRentRequestValidator.validate(roomRentRequest, meetingroom.getCapacity(),meetingroom.getAvailability())){
            throw new IllegalArgumentException("Invalid room rent request");
        }
        var roomRent = RoomRent.builder().userId(userId)
                .id(id)
                .rentStart(roomRentRequest.getRentStart())
                .rentEnd(roomRentRequest.getRentEnd())
                .meetingRoom(meetingroom)
                .duration(roomRentRequest.getDuration())
                .cost(meetingroom.calculateCost(roomRentRequest.getDuration()))
                .build();
        roomRentRepository.save(roomRent);

        var listOfAttendeeStatusInDB = attendeeStatusRepository.findAllByRentId(id);
        roomRentRequest.getAttendeeStatusList().forEach(details -> attendeeStatusRepository.save(
                AttendeeStatus.builder()
                        .rentId(roomRent.getId())
                        .name(details.getName())
                        .statusType(details.getStatusType())
                        .build()
        ));
        attendeeStatusRepository.deleteAll(listOfAttendeeStatusInDB);
        return roomRent;
    }


    @Override
    public RoomRentAdminResponse findRoomRentById(Integer id) {
        if (isRoomRentDoesNotExist(id)) throw new RoomRentDoesNotExistException(id);
        var roomrent = roomRentRepository.findRoomRentById(id);
        return RoomRentAdminResponse.fromRoomRent(roomrent, attendeeStatusRepository.findAllByRentId(id));
    }


    @Override
    public void delete(Integer id) {
        if (isRoomRentDoesNotExist(id)) throw new RoomRentDoesNotExistException(id);
        else roomRentRepository.deleteById(id);
    }

    private boolean isRoomRentDoesNotExist(Integer id) {
        return roomRentRepository.findRoomRentById(id)==null;
    }

    private boolean isMeetingRoomDoesNotExist(Integer id) {
        return meetingRoomRepository.findMeetingRoomById(id)==null;
    }
}
