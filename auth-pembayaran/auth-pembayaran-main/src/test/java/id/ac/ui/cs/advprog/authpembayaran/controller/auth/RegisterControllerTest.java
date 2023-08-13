package id.ac.ui.cs.advprog.authpembayaran.controller.auth;

import id.ac.ui.cs.advprog.authpembayaran.Util;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.*;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;

import id.ac.ui.cs.advprog.authpembayaran.auth.service.RegisterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RegisterServiceImpl registerService;

    @Mock
    User user;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void testRegister() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        RegisterResponse registerResponse = new RegisterResponse();

        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("Password1!");
        registerRequest.setName("Tester");
        String requestBody = Util.mapToJson(registerRequest);

        when(registerService.register(any(RegisterRequest.class))).thenReturn(registerResponse);

        mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("register"))
                .andExpect(content().json(Util.mapToJson(registerResponse)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        verify(registerService, atLeastOnce()).register(any(RegisterRequest.class));
    }

}