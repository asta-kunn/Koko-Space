package id.ac.ui.cs.advprog.coworkingspace.controller;

import id.ac.ui.cs.advprog.coworkingspace.Util;
import id.ac.ui.cs.advprog.coworkingspace.auth.model.JwtPayload;
import id.ac.ui.cs.advprog.coworkingspace.auth.service.JwtService;
import id.ac.ui.cs.advprog.coworkingspace.workspace.controller.SpaceRentController;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentResponse;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.WorkspaceType;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent.SpaceRentServiceImpl;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SpaceRentController.class)
@AutoConfigureMockMvc
class SpaceRentControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private SpaceRentServiceImpl service;

    @MockBean
    private JwtService jwtService;

    SpaceRent rent;
    Object bodyContent, invalidBodyContent;
    Workspace workspace;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                null,
                new JwtPayload(1, "PENGELOLA", "agun", true, 2000000.0));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


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

        rent = SpaceRent.builder()
                .userId(1)
                .rentStart(null)
                .rentEnd(null)
                .cost(125000.0)
                .workspace(workspace)
                .duration(1L)
                .build();

        bodyContent = new Object() {
            public final Integer id = 1;
            public final Integer userId = 1;
            public final LocalDateTime rentStart = null;
            public final LocalDateTime rentEnd = null;
            public final Double cost = 125000.0;
            public final Long duration = 1L;
            public final Integer workspaceId = 1;
        };

        invalidBodyContent = new Object() {
            public final Integer id = 1;
            public final Integer userId = 1;
            public final LocalDateTime rentStart = null;
            public final LocalDateTime rentEnd = null;
            public final Double cost = 125000.0;
            public final Long duration = -1L;
            public final Integer workspaceId = -1;
        };
    }

    @Test
    @WithMockUser()
    void testGetAllSpaceRent() throws Exception {
        SpaceRentResponse rentAdminResponse = new SpaceRentResponse();
        rentAdminResponse.setSpaceRentId(1);

        when(service.findAll()).thenReturn(List.of(rentAdminResponse));

        mvc.perform(get("/api/coworking-space/space-rent/all"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllSpaceRent"))
                .andExpect(jsonPath("$[0].spaceRentId").value(rentAdminResponse.getSpaceRentId()));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser()
    void testGetAllUserSpaceRent() throws Exception {
        SpaceRentResponse rentUserResponse = new SpaceRentResponse();
        rentUserResponse.setSpaceRentId(1);

        when(service.findAllByUserId(any(Integer.class))).thenReturn(List.of(rentUserResponse));

        mvc.perform(get("/api/coworking-space/space-rent/me")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllUserSpaceRent"))
                .andExpect(jsonPath("$[0].spaceRentId").value(rentUserResponse.getSpaceRentId()));

        verify(service, atLeastOnce()).findAllByUserId(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void testGetAllSpaceRentById() throws Exception {
        SpaceRentResponse rentResponse = new SpaceRentResponse();
        rentResponse.setSpaceRentId(1);

        when(service.getSpaceRentById(any(Integer.class))).thenReturn(rentResponse);

        mvc.perform(get("/api/coworking-space/space-rent/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getSpaceRentById"))
                .andExpect(jsonPath("$.spaceRentId").value(rentResponse.getSpaceRentId()));

        verify(service, atLeastOnce()).getSpaceRentById(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void testCreateSpaceRent() throws Exception {
        when(service.create(any(Integer.class), any(SpaceRentRequest.class))).thenReturn(rent);

        mvc.perform(post("/api/coworking-space/space-rent/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createSpaceRent"))
                .andExpect(jsonPath("$.id").value(rent.getId()));

        verify(service, atLeastOnce()).create(any(Integer.class), any(SpaceRentRequest.class));
    }

    @Test
    @WithMockUser()
    void testExtendSpaceRent() throws Exception {
        mvc.perform(put("/api/coworking-space/space-rent/extend/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("extendSpaceRent"));

        verify(service, atLeastOnce()).extendRent(any(Integer.class), any(Integer.class));
    }

    @Test
    @WithMockUser()
    void testUpgradeSpaceRent() throws Exception {
        workspace.setType(WorkspaceType.PERSONAL);
        when(service.upgrade(any(Integer.class), any(Integer.class))).thenReturn(rent);

        mvc.perform(put("/api/coworking-space/space-rent/upgrade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("upgradeSpaceRent"))
                .andExpect(jsonPath("$.id").value(rent.getId()));

        verify(service, atLeastOnce()).upgrade(any(Integer.class), any(Integer.class));
    }

    @Test
    @WithMockUser()
    void testDeleteSpaceRent() throws Exception {
        mvc.perform(delete("/api/coworking-space/space-rent/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteSpaceRent"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenGetSpaceRentByIdButInvalidIdShouldReturnBadRequest() throws Exception {
        mvc.perform(get("/api/coworking-space/space-rent/id/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("getSpaceRentById"));

        verify(service, never()).getSpaceRentById(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenCreateSpaceRentButInvalidRequestShouldReturnBadRequest() throws Exception {
        mvc.perform(post("/api/coworking-space/space-rent/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(invalidBodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("createSpaceRent"));

        verify(service, never()).create(any(Integer.class), any(SpaceRentRequest.class));
    }

    @Test
    @WithMockUser()
    void whenDeleteSpaceRentButInvalidId() throws Exception {
        mvc.perform(delete("/api/coworking-space/space-rent/delete/-1")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("deleteSpaceRent"));

        verify(service, never()).delete(any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenExtendSpaceRentButInvalidId() throws Exception {
        mvc.perform(put("/api/coworking-space/space-rent/extend/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("extendSpaceRent"));

        verify(service, never()).extendRent(any(Integer.class), any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenExtendSpaceRentButInvalidRequest() throws Exception {
        mvc.perform(put("/api/coworking-space/space-rent/extend/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(invalidBodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("extendSpaceRent"));

        verify(service, never()).extendRent(any(Integer.class), any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenUpgradeSpaceRentButInvalidId() throws Exception {
        mvc.perform(put("/api/coworking-space/space-rent/upgrade/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("upgradeSpaceRent"));

        verify(service, never()).upgrade(any(Integer.class), any(Integer.class));
    }

    @Test
    @WithMockUser()
    void whenUpgradeSpaceRentButInvalidRequest() throws Exception {
        mvc.perform(put("/api/coworking-space/space-rent/upgrade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(invalidBodyContent))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("upgradeSpaceRent"));

        verify(service, never()).upgrade(any(Integer.class), any(Integer.class));
    }
}
