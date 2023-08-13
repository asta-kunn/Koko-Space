package id.ac.ui.cs.advprog.coworkingspace.controller;
import id.ac.ui.cs.advprog.coworkingspace.Util;
import id.ac.ui.cs.advprog.coworkingspace.auth.model.JwtPayload;
import id.ac.ui.cs.advprog.coworkingspace.auth.service.JwtService;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.controller.RoomRentController;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.AttendeeStatusList;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentAdminResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.rent.RoomRentUserResponse;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.StatusType;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.rent.RoomRentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomRentController.class)
@AutoConfigureMockMvc
class RoomRentControllerTest {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private RoomRentServiceImpl service;
    @MockBean
    private JwtService jwtService;
    RoomRent rent;
    Object bodyContent;
    RoomRentRequest rentRoomRequest;
    AttendeeStatus attendeeStatus;
    StatusType statusType;
    MeetingRoom meetingRoom;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                null,
                new JwtPayload(1, "PENGELOLA", "agun", true, 2000000.0));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        meetingRoom = new MeetingRoom();
        meetingRoom.setId(1);

        rentRoomRequest = new RoomRentRequest();
        rentRoomRequest.setMeetingRoomId(1);
        rentRoomRequest.setRentStart(new Date());
        rentRoomRequest.setRentEnd(new Date());
        rentRoomRequest.setDuration(2);
        rentRoomRequest.setCost(100000);
        rentRoomRequest.setAttendeeStatusList((List<AttendeeStatusList>) attendeeStatus);

        attendeeStatus = new AttendeeStatus();
        attendeeStatus.setId(1);
        attendeeStatus.setRentId(1);
        attendeeStatus.setName("name");
        attendeeStatus.setStatusType(statusType);

        statusType = StatusType.PRESENT;

        rent = RoomRent.builder()
                .id(1)
                .meetingRoom(meetingRoom)
                .rentStart(null)
                .rentEnd(null)
                .duration(2)
                .cost(100000)
                .attendeeStatusList(List.of(attendeeStatus))
                .build();

        bodyContent = new Object() {
            public final int id = 1;
            public final int meetingRoomId = 1;
            public final Date rentStart = null;
            public final Date rentEnd = null;
            public final int duration = 2;
            public final int cost = 100000;
            public final List<AttendeeStatusList> attendeeStatusList = List.of(AttendeeStatusList.fromAttendeeStatus(attendeeStatus));
        };
    }

    @Test
    @WithMockUser()
    void testCreateRoomRent() throws Exception {
        when(service.create(any(Integer.class), any(RoomRentRequest.class))).thenReturn(rent);

        mvc.perform(post("/api/coworking-space/room-rent/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createRoomRent"))
                .andExpect(jsonPath("$.id").value(1));

        verify(service, atLeastOnce()).create(any(Integer.class), any(RoomRentRequest.class));
    }

    @Test
    @WithMockUser()
    void testUpdateRoomRent() throws Exception {
        when(service.update(any(Integer.class), any(Integer.class), any(RoomRentRequest.class))).thenReturn(rent);

        mvc.perform(put("/api/coworking-space/room-rent/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateRoomRent"))
                .andExpect(jsonPath("$.id").value(1));

        verify(service, atLeastOnce()).update(any(Integer.class), any(Integer.class), any(RoomRentRequest.class));
    }

    @Test
    @WithMockUser()
    void testGetAllUserRoomRent() throws Exception {
        RoomRentUserResponse roomRentUserResponse = RoomRentUserResponse.builder()
                .roomRentId(1)
                .rentStart(null)
                .rentEnd(null)
                .duration(2)
                .cost(100000)
                .meetingRoom(meetingRoom)
                .attendeeStatusList(List.of(AttendeeStatusList.fromAttendeeStatus(attendeeStatus)))
                .build();

        when(service.findAllByUserId(any(Integer.class))).thenReturn(List.of(roomRentUserResponse));

        mvc.perform(get("/api/coworking-space/room-rent/me"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllUserRoomRent"))
                .andExpect(jsonPath("$[0].roomRentId").value(1));

        verify(service, atLeastOnce()).findAllByUserId(any(Integer.class));
    }

    @Test
    @WithMockUser( roles = {"PENGELOLA"})
    void testDeleteRoomRent() throws Exception {
        doNothing().when(service).delete(any());

        mvc.perform(delete("/api/coworking-space/room-rent/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(any());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PENGELOLA"})
    void testGetRoomRentById() throws Exception {
        when(service.findRoomRentById(any())).thenReturn(new RoomRentAdminResponse());

        mvc.perform(get("/api/coworking-space/room-rent/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).findRoomRentById(any());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PENGELOLA"})
    void testGetAllRoomRent() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/coworking-space/room-rent/all"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAll();
    }

}
