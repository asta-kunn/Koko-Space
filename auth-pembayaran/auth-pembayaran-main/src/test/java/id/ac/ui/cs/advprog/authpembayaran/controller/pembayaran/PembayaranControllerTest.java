package id.ac.ui.cs.advprog.authpembayaran.controller.pembayaran;

import id.ac.ui.cs.advprog.authpembayaran.Util;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.JwtServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.UserServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranResponse;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.PembayaranServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
class PembayaranControllerTest {
        @Autowired
        private MockMvc mvc;

        @MockBean
        private PembayaranServiceImpl pembayaranService;

        @MockBean
        private UserServiceImpl userService;

        @Autowired
        private JwtServiceImpl jwtService;

        @Mock
        private UserRepository repository;
        @Mock
        User user;

        @BeforeEach
        void setUp() {
                user = User.builder().id(1).saldo(2000.0).email("test@test.com").name("test").active(true)
                                .verified(true)
                                .role(Role.PELANGGAN).build();

                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(null);

                SecurityContextHolder.setContext(securityContext);

                repository.save(user);

        }

        @Test
        @WithMockUser(roles = "PELANGGAN")
        void testPembayaran() throws Exception {

                PembayaranRequest pembayaranRequest = new PembayaranRequest();
                PembayaranResponse pembayaranResponse = new PembayaranResponse();
                String requestBody = Util.mapToJson(pembayaranRequest);
                HashMap<String, Object> claims = new HashMap<>();

                claims.put("role", user.getRole());
                claims.put("id", user.getId());
                claims.put("saldo", user.getSaldo());
                claims.put("verified", user.isVerified());
                claims.put("active", user.isActive());
                claims.put("name", user.getName());

                String token = jwtService.generateToken(claims, user);
                String authorizationHeader = "Bearer " + token;
                when(pembayaranService.pay(anyInt(), any(PembayaranRequest.class))).thenReturn(pembayaranResponse);
                mvc.perform(post("/pay")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(handler().methodName("pay"))
                                .andExpect(content().json(Util.mapToJson(pembayaranResponse)))
                                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

                verify(pembayaranService, atLeastOnce()).pay(anyInt(), any(PembayaranRequest.class));
        }

}
