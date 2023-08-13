package id.ac.ui.cs.advprog.authpembayaran.controller.auth;

import id.ac.ui.cs.advprog.authpembayaran.Util;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.*;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;

import id.ac.ui.cs.advprog.authpembayaran.auth.service.LoginServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.UserServiceImpl;
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
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private UserServiceImpl userService;

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
    void testLogin() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        LoginResponse loginResponse = new LoginResponse();

        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("passtest");
        String requestBody = Util.mapToJson(loginRequest);

        when(loginService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("login"))
                .andExpect(content().json(Util.mapToJson(loginResponse)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        verify(loginService, atLeastOnce()).login(any(LoginRequest.class));
    }

    @Test
    void testChangePassword() throws Exception {

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        String response = "";

        changePasswordRequest.setSecretToken(
                "KI+SGfOaFUTsH7eSMiI6CWkydIN1PR5YVW+9SPSexPbc96FEy2T9s1t5gPvYA4Gk9Oju7lPrAriMdJjpoljj5g==");
        changePasswordRequest.setNewPassword("newpass");
        String requestBody = Util.mapToJson(changePasswordRequest);

        when(userService.changePassword(any(ChangePasswordRequest.class))).thenReturn(response);

        mvc.perform(patch("/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("changePassword"))
                .andExpect(content().string(response))
                .andExpect(
                        header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=ISO-8859-1"))
                .andReturn();

        verify(userService, atLeastOnce()).changePassword(any(ChangePasswordRequest.class));
    }

    @Test
    void testForgotPassword() throws Exception {

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();

        forgotPasswordRequest.setEmail("test@test.com");

        String requestBody = Util.mapToJson(forgotPasswordRequest);

        when(loginService.forgotPassword(any(ForgotPasswordRequest.class))).thenReturn(forgotPasswordResponse);

        mvc.perform(post("/user/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("forgotPassword"))
                .andExpect(content().json(Util.mapToJson(forgotPasswordResponse)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        verify(loginService, atLeastOnce()).forgotPassword(any(ForgotPasswordRequest.class));
    }
}
