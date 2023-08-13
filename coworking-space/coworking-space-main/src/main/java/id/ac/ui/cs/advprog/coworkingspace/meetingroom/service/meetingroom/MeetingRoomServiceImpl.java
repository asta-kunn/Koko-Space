package id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.meetingroom;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.MeetingRoomRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.exception.MeetingRoomDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoomType;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.MeetingRoomRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.validator.meetingroom.MeetingRoomRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingRoomServiceImpl implements MeetingRoomService {
    private final MeetingRoomRepository meetingRoomRepository;
    @Override
    public List<MeetingRoom> findAll() {
        return meetingRoomRepository.findAll();
    }

    @Override
    public MeetingRoom findMeetingRoomById(Integer id) {
        var meetingroom = meetingRoomRepository.findMeetingRoomById(id);
        if (isMeetingRoomDoesNotExist(id)) throw new MeetingRoomDoesNotExistException(id);
        return meetingroom;
    }

    @Override
    public MeetingRoom create(MeetingRoomRequest request) {
        if (!MeetingRoomRequestValidator.validate(request)) {
            throw new IllegalArgumentException("Invalid meeting room request");
        }
        MeetingRoom meetingRoom = MeetingRoom.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .type(MeetingRoomType.valueOf(request.getType()))
                .city(request.getCity())
                .country(request.getCountry())
                .address(request.getAddress())
                .images(request.getImages())
                .price(request.getPrice())
                .availability(request.getAvailability())
                .build();

        return meetingRoomRepository.save(meetingRoom);
    }

    @Override
    public MeetingRoom update(Integer id, MeetingRoomRequest request) {
        if (isMeetingRoomDoesNotExist(id)) throw new MeetingRoomDoesNotExistException(id);

        if (!MeetingRoomRequestValidator.validate(request)) {
            throw new IllegalArgumentException("Invalid meeting room request");
        }

        MeetingRoom meetingRoom = MeetingRoom.builder()
                .id(id)
                .name(request.getName())
                .capacity(request.getCapacity())
                .type(MeetingRoomType.valueOf(request.getType()))
                .city(request.getCity())
                .country(request.getCountry())
                .address(request.getAddress())
                .images(request.getImages())
                .price(request.getPrice())
                .availability(request.getAvailability())
                .build();

        return meetingRoomRepository.save(meetingRoom);
    }

    @Override
    public void delete(Integer id) {
        if (isMeetingRoomDoesNotExist(id)) throw new MeetingRoomDoesNotExistException(id);
        else meetingRoomRepository.deleteById(id);
    }

    private boolean isMeetingRoomDoesNotExist(Integer id) {
        return meetingRoomRepository.findMeetingRoomById(id)==null;
    }
}
