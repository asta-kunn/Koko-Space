package id.ac.ui.cs.advprog.coworkingspace.service.rent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentAdminResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.AttendeeStatusList;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentUserResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.exception.MeetingRoomDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.exceptions.RoomRentDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoomType;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.StatusType;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.AttendeeStatusRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.rent.RoomRentServiceImpl;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.RoomRentRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.MeetingRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Objects;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RoomRentServiceImplTest {
    @InjectMocks
    private RoomRentServiceImpl service;
    @Mock
    private RoomRentRepository roomRentRepository;
    @Mock
    private AttendeeStatusRepository attendeeStatusRepository;
    @Mock
    private MeetingRoomRepository meetingRoomRepository;
    RoomRent roomRent;
    RoomRentRequest rentRoomRequest, rentRoomRequestWrong, rentRoomRequestWrongAvailable, rentRoomRequestWrongExceedCapacity;
    AttendeeStatusList attendeeStatusList;

    MeetingRoom meetingRoom, meetingRoomNotAvailable;


    @BeforeEach
    void setUp() {
        meetingRoom  = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.valueOf("MEDIUM"))
                .name("Meeting Room 1")
                .price(100000)
                .images(List.of("image1", "image2"))
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .capacity(1)
                .availability(true)
                .build();

        meetingRoomNotAvailable  = MeetingRoom.builder()
                .id(2)
                .type(MeetingRoomType.valueOf("MEDIUM"))
                .name("Meeting Room 1")
                .price(100000)
                .images(List.of("image1", "image2"))
                .city("Depok")
                .country("Indonesia")
                .address("Pocin")
                .capacity(10)
                .availability(false)
                .build();

        rentRoomRequest = RoomRentRequest.builder()
                .meetingRoomId(1)
                .rentStart(new Date())
                .rentEnd(new Date())
                .duration(1)
                .cost(100000)
                .attendeeStatusList(List.of(AttendeeStatusList.fromAttendeeStatus(AttendeeStatus.builder()
                        .statusType(StatusType.PRESENT)
                        .name("Attendee 1")
                        .build())))
                .build();

        rentRoomRequestWrongExceedCapacity = RoomRentRequest.builder()
                .meetingRoomId(1)
                .rentStart(new Date())
                .rentEnd(new Date())
                .duration(1)
                .cost(100000)
                .attendeeStatusList(List.of(
                        AttendeeStatusList.fromAttendeeStatus(AttendeeStatus.builder()
                                .statusType(StatusType.PRESENT)
                                .name("Attendee 1")
                                .build()),
                        AttendeeStatusList.fromAttendeeStatus(AttendeeStatus.builder()
                                .statusType(StatusType.PRESENT)
                                .name("Attendee 2")
                                .build())
                ))
                .build();

        rentRoomRequestWrongAvailable = RoomRentRequest.builder()
                .meetingRoomId(2)
                .rentStart(new Date())
                .rentEnd(new Date())
                .duration(1)
                .cost(100000)
                .attendeeStatusList(List.of(AttendeeStatusList.fromAttendeeStatus(AttendeeStatus.builder()
                        .statusType(StatusType.PRESENT)
                        .name("Attendee 1")
                        .build())))
                .build();

        rentRoomRequestWrong = RoomRentRequest.builder()
                .meetingRoomId(1)
                .rentStart(new Date())
                .rentEnd(new Date())
                .duration(3)
                .cost(100000)
                .attendeeStatusList(List.of(AttendeeStatusList.fromAttendeeStatus(AttendeeStatus.builder()
                        .statusType(StatusType.PRESENT)
                        .name("Attendee 1")
                        .build())))
                .build();

        attendeeStatusList = AttendeeStatusList.builder()
                .statusType(StatusType.PRESENT)
                .name("Attendee 1")
                .build();

        roomRent = RoomRent.builder()
                .id(1)
                .userId(1)
                .rentStart(new Date(2023, 6, 3))
                .rentEnd(new Date(2023, 6, 7))
                .duration(5)
                .cost(500000)
                .attendeeStatusList(List.of(AttendeeStatus.builder()
                        .statusType(StatusType.PRESENT)
                        .name("Attendee 1")
                        .build()))
                .meetingRoom(meetingRoom)
                .build();
    }

    @Test
    void whenCreateRoomRentShouldCallRepositorySave() {
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(roomRent.getMeetingRoom());
        service.create(1, rentRoomRequest);
        verify(roomRentRepository, times(1)).save(any(RoomRent.class));
    }

    @Test
    void whenCreateRoomRentButMeetingRoomNotFoundShouldThrowException() {
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(null);
        assertThrows(MeetingRoomDoesNotExistException.class, () -> {
            service.create(0, rentRoomRequest);
        });
    }

    @Test
    void whenCreateRoomRentButNotPassValidator() {
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(roomRent.getMeetingRoom());
        assertThrows(IllegalArgumentException.class, () -> {
            service.create(1, rentRoomRequestWrong);
        });
    }
    @Test
    void whenUpdateRoomRentButNotPassValidator() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        assertThrows(IllegalArgumentException.class, () -> {
            service.update(1, 1, rentRoomRequestWrong);
        });
    }

    @Test
    void whenCreateRoomRentButNotPassValidatorAvailable() {
        when(meetingRoomRepository.findMeetingRoomById(2)).thenReturn(meetingRoomNotAvailable);

        assertThrows(IllegalArgumentException.class, () -> {
            service.create(1, rentRoomRequestWrongAvailable);
        });
    }

    @Test
    void whenUpdateRoomRentButNotPassValidatorAvailable() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        when(meetingRoomRepository.findMeetingRoomById(2)).thenReturn(meetingRoomNotAvailable);
        assertThrows(IllegalArgumentException.class, () -> {
            service.update(1, 1, rentRoomRequestWrongAvailable);
        });
    }

    @Test
    void whenUpdateRoomRentButNotPassValidatorExceedCapacity() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        assertThrows(IllegalArgumentException.class, () -> {
            service.update(1, 1, rentRoomRequestWrongExceedCapacity);
        });
    }

    @Test
    void whenCreateRoomRentButNotPassValidatorExceedCapacity() {
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(roomRent.getMeetingRoom());
        assertThrows(IllegalArgumentException.class, () -> {
            service.create(1, rentRoomRequestWrongExceedCapacity);
        });
    }

    @Test
    void whenUpdateRoomRentButNotFoundShouldThrowException() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(null);
        assertThrows(RoomRentDoesNotExistException.class, () -> {
            service.update(0, 0, rentRoomRequest);
        });
    }

    @Test
    void whenDeleteRoomRentShouldCallRepositoryDelete() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        service.delete(1);
        verify(roomRentRepository, times(1)).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteRoomRentButNotFoundShouldThrowException() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(null);
        assertThrows(RoomRentDoesNotExistException.class, () -> {
            service.delete( 0);
        });
    }

    @Test
    void whenFindAllRoomRentShouldCallRepositoryFindAll() {
        service.findAll();
        verify(roomRentRepository, times(1)).findAll();
    }

    @Test
    void whenFindRoomRentByIdShouldReturnRoomRentResponse() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        RoomRentAdminResponse roomRentAdminResponse = service.findRoomRentById(1);
        assert Objects.equals(roomRentAdminResponse.getRoomRentId(), roomRent.getId());
        verify(roomRentRepository, atLeastOnce()).findRoomRentById(any(Integer.class));
    }

    @Test
    void whenFindRoomRentByIdAndNotFoundShouldThrowException() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(null);
        assertThrows(RoomRentDoesNotExistException.class, () -> {
            service.findRoomRentById(0);
        });
    }

    @Test
    void whenFindAllByUserIdShouldReturnListOfRoomRentUserResponse() {
        when(roomRentRepository.findAllByUserId(any(Integer.class))).thenReturn(List.of(roomRent));
        List<RoomRentUserResponse> roomRentAdminResponse = service.findAllByUserId(1);
        assert Objects.equals(roomRentAdminResponse.get(0).getRoomRentId(), roomRent.getId());
        verify(roomRentRepository, atLeastOnce()).findAllByUserId(any(Integer.class));
    }
    @Test
    void whenUpdateRoomRentShouldUpdateRecord() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        service.update(1, 1, rentRoomRequest);
        verify(roomRentRepository, times(1)).save(any(RoomRent.class));
    }
    @Test
    void whenUpdateRoomRentButMeetingRoomNotFoundShouldThrowException() {
        when(roomRentRepository.findRoomRentById(any(Integer.class))).thenReturn(roomRent);
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(null);
        assertThrows(MeetingRoomDoesNotExistException.class, () -> {
            service.update(1, 1, rentRoomRequest);
        });
    }
    @Test
    void whenFindAllShouldReturnListOfRoomRentAdminResponse() {
        when(roomRentRepository.findAll()).thenReturn(List.of(roomRent));
        List<RoomRentAdminResponse> roomRentAdminResponses = service.findAll();
        assert Objects.equals(roomRentAdminResponses.get(0).getRoomRentId(), roomRent.getId());
        verify(roomRentRepository, atLeastOnce()).findAll();
    }

}
