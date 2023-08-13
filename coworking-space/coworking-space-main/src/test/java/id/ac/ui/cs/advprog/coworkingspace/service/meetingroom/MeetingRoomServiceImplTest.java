package id.ac.ui.cs.advprog.coworkingspace.service.meetingroom;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.MeetingRoomRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.exception.MeetingRoomDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoomType;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository.MeetingRoomRepository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.meetingroom.MeetingRoomServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingRoomServiceImplTest {
    @InjectMocks
    private MeetingRoomServiceImpl service;

    @Mock
    private MeetingRoomRepository meetingRoomRepository;

    MeetingRoom meetingRoom;
    MeetingRoom updatedMeetingRoom;
    MeetingRoomRequest createRequest, createRequestWrong, createRequestWrongType;
    MeetingRoomRequest updateRequest, updateRequestWrong, updateRequestWrongType;
    @BeforeEach
    void setUp() {
        createRequest = MeetingRoomRequest.builder()
                .type("SMALL")
                .name("Meeting Room 1")
                .capacity(10)
                .price(150000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        updateRequest = MeetingRoomRequest.builder()
                .type("MEDIUM")
                .name("Meeting Room 1")
                .capacity(30)
                .price(200000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        meetingRoom = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.SMALL)
                .name("Meeting Room 1")
                .capacity(10)
                .price(150000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        updatedMeetingRoom = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.MEDIUM)
                .name("Meeting Room 1")
                .capacity(30)
                .price(200000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        createRequestWrong = MeetingRoomRequest.builder()
                .type("SMALL")
                .name("Meeting Room 1")
                .capacity(10)
                .price(-150000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        updateRequestWrong = MeetingRoomRequest.builder()
                .type("MEDIUM")
                .name("Meeting Room 1")
                .capacity(-3)
                .price(200000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        createRequestWrongType = MeetingRoomRequest.builder()
                .type("SMALLER")
                .name("Meeting Room 1")
                .capacity(10)
                .price(150000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();

        updateRequestWrongType = MeetingRoomRequest.builder()
                .type("LARGER")
                .name("Meeting Room 1")
                .capacity(30)
                .price(200000)
                .city("Jakarta")
                .country("Indonesia")
                .address("Jalan Raya")
                .availability(true)
                .images(List.of("image1", "image2"))
                .build();
    }

    @Test
    void whenFindAllMeetingRoomShouldReturnListOfMeetingRoom(){
        List<MeetingRoom> allMeetingRoom = List.of(meetingRoom);
        when(meetingRoomRepository.findAll()).thenReturn(allMeetingRoom);

        List<MeetingRoom> result = service.findAll();
        verify(meetingRoomRepository, atLeastOnce()).findAll();
        Assertions.assertEquals(allMeetingRoom, result);
    }

    @Test
    void whenFindMeetingRoomByIdAndShouldReturnMeetingRoom(){
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);

        MeetingRoom result = service.findMeetingRoomById(0);
        verify(meetingRoomRepository, atLeastOnce()).findMeetingRoomById(any(Integer.class));
        Assertions.assertEquals(meetingRoom, result);
    }

    @Test
    void whenFindMeetingRoomByIdAndNotFoundShouldThrowException(){
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(null);

        Assertions.assertThrows(MeetingRoomDoesNotExistException.class, () ->{
            service.findMeetingRoomById(0);
        });
    }
    @Test
    void whenCreateMeetingRoomShouldReturnTheCreatedMeetingRoom(){
        when(meetingRoomRepository.save(any(MeetingRoom.class))).thenAnswer(i ->{
            var meetingRoom = i.getArgument(0, MeetingRoom.class);
            meetingRoom.setId(1);
            return meetingRoom;
        });

        MeetingRoom result = service.create(createRequest);
        verify(meetingRoomRepository, atLeastOnce()).save(any(MeetingRoom.class));
        Assertions.assertEquals(meetingRoom, result);
    }

    @Test
    void whenCreateMeetingRoomButNotPassValidator() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.create(createRequestWrong);
        });
    }

    @Test
    void whenUpdateMeetingRoomButNotPassValidator() {
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.update(1, updateRequestWrong);
        });
    }

    @Test
    void whenCreateMeetingRoomButNotPassValidatorType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.create(createRequestWrongType);
        });
    }

    @Test
    void whenUpdateMeetingRoomButNotPassValidatorType() {
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.update(1, updateRequestWrongType);
        });
    }

    @Test
    void whenUpdateMeetingRoomAndFoundShouldReturnTheUpdateMeetingRoom(){
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        when(meetingRoomRepository.save(any(MeetingRoom.class))).thenAnswer(i ->
            i.getArgument(0, MeetingRoom.class));

        MeetingRoom result = service.update(1, updateRequest);
        verify(meetingRoomRepository, atLeastOnce()).save(any(MeetingRoom.class));
        Assertions.assertEquals(updatedMeetingRoom, result);
    }

    @Test
    void whenUpdateMeetingRoomAndNotFoundShouldThrowException(){
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(null);
        Assertions.assertThrows(MeetingRoomDoesNotExistException.class, () ->{
            service.update(0, updateRequest);
        });
    }

    @Test
    void whenDeleteMeetingRoomAndFoundShouldCallDeleteByIdOnRepo(){
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(meetingRoom);
        service.delete(0);
        verify(meetingRoomRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteMeetingRoomAndNotFoundShouldThrowException(){
        when(meetingRoomRepository.findMeetingRoomById(any(Integer.class))).thenReturn(null);
        Assertions.assertThrows(MeetingRoomDoesNotExistException.class, () ->{
            service.delete(0);
        });
    }
}
