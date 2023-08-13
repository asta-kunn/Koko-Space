package id.ac.ui.cs.advprog.coworkingspace.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.coworkingspace.auth.service.JwtService;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto.MeetingRoomRequest;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoomType;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.service.meetingroom.MeetingRoomServiceImpl;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.controller.MeetingRoomController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MeetingRoomController.class)
@AutoConfigureMockMvc
class MeetingRoomControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MeetingRoomServiceImpl meetingRoomService;
    @MockBean
    private JwtService jwtService;

    MeetingRoom meetingRoom;
    Object bodyContent;

    @BeforeEach
    public void setUp(){
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        meetingRoom = MeetingRoom.builder()
                .type(MeetingRoomType.SMALL)
                .capacity(10)
                .price(100000)
                .build();

        bodyContent = new Object(){
            public final String type = "SMALL";
            public final int capacity = 10;
            public final int price = 100000;
        };

    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void testDeleteMeetingRoom() throws Exception{
        mvc.perform(delete("/api/coworking-space/meetingroom/delete/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteMeetingRoom"));

        verify(meetingRoomService, atLeastOnce()).delete(any(Integer.class));
    }

    @Test
    @WithMockUser
    void testGetAllMeetingRoom() throws Exception {
        MeetingRoom meetingRoom1 = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.SMALL)
                .capacity(10)
                .price(100000)
                .build();
        MeetingRoom meetingRoom2 = MeetingRoom.builder()
                .id(2)
                .type(MeetingRoomType.MEDIUM)
                .capacity(20)
                .price(200000)
                .build();

        List<MeetingRoom> meetingRooms = List.of(meetingRoom1, meetingRoom2);
        when(meetingRoomService.findAll()).thenReturn(meetingRooms);

        mvc.perform(get("/api/coworking-space/meetingroom/all"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllMeetingRoom"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("SMALL"))
                .andExpect(jsonPath("$[0].capacity").value(10))
                .andExpect(jsonPath("$[0].price").value(100000))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].type").value("MEDIUM"))
                .andExpect(jsonPath("$[1].capacity").value(20))
                .andExpect(jsonPath("$[1].price").value(200000));

        verify(meetingRoomService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetMeetingRoomById() throws Exception {
        MeetingRoom meetingRoom = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.SMALL)
                .capacity(10)
                .price(100000)
                .build();
        when(meetingRoomService.findMeetingRoomById(1)).thenReturn(meetingRoom);

        mvc.perform(get("/api/coworking-space/meetingroom/id/1"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getMeetingRoomById"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("SMALL"))
                .andExpect(jsonPath("$.capacity").value(10))
                .andExpect(jsonPath("$.price").value(100000));

        verify(meetingRoomService, times(1)).findMeetingRoomById(1);
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void testAddMeetingRoom() throws Exception {
        MeetingRoomRequest request = MeetingRoomRequest.builder()
                .type("SMALL")
                .capacity(10)
                .price(100000)
                .build();

        MeetingRoom createdMeetingRoom = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.SMALL)
                .capacity(10)
                .price(100000)
                .build();

        when(meetingRoomService.create(any(MeetingRoomRequest.class))).thenReturn(createdMeetingRoom);

        mvc.perform(post("/api/coworking-space/meetingroom/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addMeetingRoom"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("SMALL"))
                .andExpect(jsonPath("$.capacity").value(10))
                .andExpect(jsonPath("$.price").value(100000));

        verify(meetingRoomService, times(1)).create(any(MeetingRoomRequest.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void testUpdateMeetingRoom() throws Exception {
        MeetingRoomRequest request = MeetingRoomRequest.builder()
                .type("SMALL")
                .capacity(10)
                .price(100000)
                .build();

        MeetingRoom updatedMeetingRoom = MeetingRoom.builder()
                .id(1)
                .type(MeetingRoomType.SMALL)
                .capacity(10)
                .price(100000)
                .build();

        when(meetingRoomService.update(eq(1), any(MeetingRoomRequest.class))).thenReturn(updatedMeetingRoom);

        mvc.perform(put("/api/coworking-space/meetingroom/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateMeetingRoom"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("SMALL"))
                .andExpect(jsonPath("$.capacity").value(10))
                .andExpect(jsonPath("$.price").value(100000));

        verify(meetingRoomService, times(1)).update(eq(1), any(MeetingRoomRequest.class));
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
