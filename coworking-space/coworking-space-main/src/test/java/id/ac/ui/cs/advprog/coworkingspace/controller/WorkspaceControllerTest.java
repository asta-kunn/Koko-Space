package id.ac.ui.cs.advprog.coworkingspace.controller;

import id.ac.ui.cs.advprog.coworkingspace.Util;
import id.ac.ui.cs.advprog.coworkingspace.auth.service.JwtService;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.WorkspaceRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent.SpaceRentServiceImpl;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.workspace.WorkspaceServiceImpl;
import id.ac.ui.cs.advprog.coworkingspace.workspace.controller.WorkspaceController;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.WorkspaceType;
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

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WorkspaceController.class)
@AutoConfigureMockMvc
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    WorkspaceController workspaceController;

    @MockBean
    private WorkspaceServiceImpl service;

    @MockBean
    private SpaceRentServiceImpl spaceRentService;

    @MockBean
    private JwtService jwtService;

    Workspace workspace;

    Object bodyContent, invalidBodyContent;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        workspace = Workspace.builder()
                .type(WorkspaceType.COWORKING)
                .capacity(25)
                .hourlyPrice(125000.00)
                .dailyPrice(1200000.00)
                .filledSeat(0)
                .description("Test")
                .image("test")
                .availability(true)
                .build();

        bodyContent = new Object() {
            public final String type = "COWORKING";
            public final Integer capacity = 25;
            public final Double hourlyPrice = 125000.00;
            public final Double dailyPrice = 1200000.00;
            public final Integer filledSeat = 0;
            public final String description = "Test";
            public final String image = "test";
            public final Boolean availability = true;
        };

        invalidBodyContent = new Object() {
            public final String type = "INVALID";
            public final Integer capacity = -25;
            public final Double hourlyPrice = -125000.00;
            public final Double dailyPrice = -1200000.00;
            public final Integer filledSeat = 0;
            public final String description = "Test";
            public final String image = "test";
            public final Boolean availability = true;
        };
    }

    @Test
    @WithMockUser()
    void testGetAllWorkspace() throws Exception {
        List<Workspace> allWorkspaces = List.of(workspace);

        when(service.findAll()).thenReturn(allWorkspaces);

        mvc.perform(get("/api/coworking-space/workspace/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllWorkspace"))
                .andExpect(jsonPath("$[0].id").value(workspace.getId()));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser()
    void testGetWorkspaceById() throws Exception {
        when(service.findById(any(Integer.class))).thenReturn(workspace);

        mvc.perform(get("/api/coworking-space/workspace/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getWorkspaceById"))
                .andExpect(jsonPath("$.id").value(workspace.getId()));


        verify(service, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void testAddWorkspace() throws Exception {
        when(service.create(any(WorkspaceRequest.class))).thenReturn(workspace);

        mvc.perform(post("/api/coworking-space/workspace/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addWorkspace"))
                .andExpect(jsonPath("$.id").value(workspace.getId()));

        verify(service, atLeastOnce()).create(any(WorkspaceRequest.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void testUpdateWorkspace() throws Exception {
        when(service.update(any(Integer.class), any(WorkspaceRequest.class))).thenReturn(workspace);

        mvc.perform(put("/api/coworking-space/workspace/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateWorkspace"))
                .andExpect(jsonPath("$.id").value(workspace.getId()));

        verify(service, atLeastOnce()).update(any(Integer.class), any(WorkspaceRequest.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void testDeleteWorkspace() throws Exception {
        mvc.perform(delete("/api/coworking-space/workspace/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteWorkspace"));

    }
    @Test
    @WithMockUser()
    void testUpdateAvailabilityWhenSeatsAvailable() throws Exception {
        // Given that the number of filled seats is less than the capacity
        workspace.setFilledSeat(workspace.getCapacity() - 1);
        when(service.findById(any(Integer.class))).thenReturn(workspace);

        // When calling getWorkspaceById
        mvc.perform(get("/api/coworking-space/workspace/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getWorkspaceById"))

                // Then the workspace should be available
                .andExpect(jsonPath("$.availability").value(true));

        verify(service, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void testGetWorkspaceByIdWhenFull() throws Exception {
        Workspace fullWorkspace = Workspace.builder()
                .type(WorkspaceType.COWORKING)
                .capacity(25)
                .hourlyPrice(125000.00)
                .dailyPrice(1200000.00)
                .filledSeat(25)
                .description("Test")
                .image("test")
                .availability(false) // Set to false since it's fully occupied
                .build();

        when(service.findById(any(Integer.class))).thenReturn(fullWorkspace);
        doNothing().when(spaceRentService).checkRentFinish(fullWorkspace.getId());

        mvc.perform(get("/api/coworking-space/workspace/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getWorkspaceById"))
                .andExpect(jsonPath("$.availability").value(false));

        verify(service, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenGetWorkspaceByIdButInvalidId() throws Exception {
        mvc.perform(get("/api/coworking-space/workspace/id/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("getWorkspaceById"));

        verify(service, never()).findById(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenAddWorkspaceButInvalidRequest() throws Exception {
        mvc.perform(post("/api/coworking-space/workspace/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(invalidBodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("addWorkspace"));

        verify(service, never()).create(any(WorkspaceRequest.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void whenUpdateWorkspaceButInvalidId() throws Exception {
        mvc.perform(put("/api/coworking-space/workspace/update/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("updateWorkspace"));

        verify(service, never()).update(any(Integer.class), any(WorkspaceRequest.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void whenUpdateWorkspaceButInvalidRequest() throws Exception {
        mvc.perform(put("/api/coworking-space/workspace/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(invalidBodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("updateWorkspace"));

        verify(service, never()).update(any(Integer.class), any(WorkspaceRequest.class));
    }

    @Test
    @WithMockUser(roles = "PENGELOLA")
    void whenDeleteWorkspaceButInvalidId() throws Exception {
        mvc.perform(delete("/api/coworking-space/workspace/delete/-1")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("deleteWorkspace"));

    }
}
