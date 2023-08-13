package id.ac.ui.cs.advprog.authpembayaran.controller.wallet;

import id.ac.ui.cs.advprog.authpembayaran.Util;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.JwtServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryStatus;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryType;
import id.ac.ui.cs.advprog.authpembayaran.wallet.repository.WalletRepository;
import id.ac.ui.cs.advprog.authpembayaran.wallet.service.WalletServiceImpl;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {
        @Autowired
        private MockMvc mvc;

        @MockBean
        private WalletServiceImpl walletService;

        @Autowired
        private JwtServiceImpl jwtService;

        @Mock
        private WalletRepository walletRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        User pengelolaUser;
        @Mock
        User pelangganUser;
        @Mock
        User nullUser;
        @Mock
        WalletHistory walletHistory;

        @BeforeEach
        void setUp() {
                pelangganUser = User.builder().id(2).saldo(2000.0).email("test@test.com").name("test").active(true)
                                .verified(true)
                                .role(Role.PELANGGAN).build();
                pengelolaUser = User.builder().id(1).saldo(10.0).email("test1@test.com").name("testAdmin").active(true)
                                .verified(true)
                                .role(Role.PENGELOLA).build();
                nullUser = User.builder().id(3).saldo(10.0).email("test2@test.com").name("testNull").active(true)
                                .verified(true)
                                .role(null).build();
                walletHistory = WalletHistory.builder()
                                .id(1)
                                .amount(10000.0)
                                .type(WalletHistoryType.PEMASUKAN)
                                .verified(false)
                                .method("PayPal")
                                .detail("Topup Deyails")
                                .createdAt(new Date())
                                .user(pelangganUser)
                                .status(WalletHistoryStatus.BEING_REVIEWED)
                                .build();
                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(null);
                SecurityContextHolder.setContext(securityContext);

                userRepository.save(pelangganUser);
                userRepository.save(pengelolaUser);
                walletRepository.save(walletHistory);
        }

        @Test
        @WithMockUser(roles = "PELANGGAN")
        void testTopup() throws Exception {

                TopupRequest topupRequest = new TopupRequest();
                TopupResponse topupResponse = new TopupResponse();
                String requestBody = Util.mapToJson(topupRequest);
                HashMap<String, Object> claims = new HashMap<>();

                claims.put("role", pelangganUser.getRole());
                claims.put("id", pelangganUser.getId());
                claims.put("saldo", pelangganUser.getSaldo());
                claims.put("verified", pelangganUser.isVerified());
                claims.put("active", pelangganUser.isActive());
                claims.put("name", pelangganUser.getName());

                String token = jwtService.generateToken(claims, pelangganUser);
                String authorizationHeader = "Bearer " + token;
                when(walletService.topup(anyInt(), any(TopupRequest.class))).thenReturn(topupResponse);
                mvc.perform(post("/wallet/topup")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(handler().methodName("topup"))
                                .andExpect(content().json(Util.mapToJson(topupResponse)))
                                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

                verify(walletService, atLeastOnce()).topup(anyInt(), any(TopupRequest.class));
        }

        @Test
        @WithMockUser(roles = "PENGELOLA")
        void testVerifyTopup() throws Exception {
                VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest();
                VerifyTopupResponse verifyTopupResponse = new VerifyTopupResponse();
                String requestBody = Util.mapToJson(verifyTopupRequest);
                HashMap<String, Object> claims = new HashMap<>();

                claims.put("role", pengelolaUser.getRole());
                claims.put("id", pengelolaUser.getId());
                claims.put("saldo", pengelolaUser.getSaldo());
                claims.put("verified", pengelolaUser.isVerified());
                claims.put("active", pengelolaUser.isActive());
                claims.put("name", pengelolaUser.getName());

                String token = jwtService.generateToken(claims, pengelolaUser);
                String authorizationHeader = "Bearer " + token;
                when(walletService.verifyTopup(any(VerifyTopupRequest.class))).thenReturn(verifyTopupResponse);

                mvc.perform(post("/wallet/verify-topup")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(handler().methodName("verifyTopup"))
                                .andExpect(content().json(Util.mapToJson(verifyTopupResponse)))
                                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

                verify(walletService, atLeastOnce()).verifyTopup(any(VerifyTopupRequest.class));
        }

        @Test
        @WithMockUser(roles = "PENGELOLA")
        void testGetAllTopupRequest() throws Exception {

                List<WalletHistory> expectedResponse = new ArrayList<>();
                expectedResponse.add(walletHistory);

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("role", pengelolaUser.getRole());
                claims.put("id", pengelolaUser.getId());
                claims.put("saldo", pengelolaUser.getSaldo());
                claims.put("verified", pengelolaUser.isVerified());
                claims.put("active", pengelolaUser.isActive());
                claims.put("name", pengelolaUser.getName());

                String token = jwtService.generateToken(claims, pengelolaUser);
                String authorizationHeader = "Bearer " + token;

                when(walletService.getAllTopupRequest()).thenReturn(expectedResponse);

                MvcResult mvcResult = mvc.perform(get("/wallet/get-all-topup-request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();

                String responseBody = mvcResult.getResponse().getContentAsString();
                assertNotNull(responseBody);
        }

        @Test
        @WithMockUser(roles = "PELANGGAN")
        void testGetAllUserWalletHistory() throws Exception {

                List<WalletHistory> expectedResponse = new ArrayList<>();
                expectedResponse.add(walletHistory);

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("role", pelangganUser.getRole());
                claims.put("id", pelangganUser.getId());
                claims.put("saldo", pelangganUser.getSaldo());
                claims.put("verified", pelangganUser.isVerified());
                claims.put("active", pelangganUser.isActive());
                claims.put("name", pelangganUser.getName());

                String token = jwtService.generateToken(claims, pelangganUser);
                String authorizationHeader = "Bearer " + token;

                when(walletService.getAllUserWalletHistory(pelangganUser.getId())).thenReturn(expectedResponse);

                MvcResult mvcResult = mvc.perform(get("/wallet/history-all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();

                // Verify the response
                String responseBody = mvcResult.getResponse().getContentAsString();
                assertNotNull(responseBody);
        }

        @Test
        @WithMockUser(roles = "PENGELOLA")
        void whenGetAllUserExpensesAsPengelolaShouldUnauthorized() throws Exception {

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("role", pengelolaUser.getRole());
                claims.put("id", pengelolaUser.getId());
                claims.put("saldo", pengelolaUser.getSaldo());
                claims.put("verified", pengelolaUser.isVerified());
                claims.put("active", pengelolaUser.isActive());
                claims.put("name", pengelolaUser.getName());

                String token = jwtService.generateToken(claims, pengelolaUser);
                String authorizationHeader = "Bearer " + token;

                // Perform the GET request
                MvcResult mvcResult = mvc.perform(get("/wallet/history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                                .andExpect(status().isUnauthorized())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();

                // Verify the response
                String responseBody = mvcResult.getResponse().getContentAsString();
                assertNotNull(responseBody);
        }

        @Test
        @WithMockUser(roles = "PENGELOLA")
        void whenGetAllUserExpensesAsPelangganShouldSuccess() throws Exception {

                WalletHistory expense = walletHistory.builder()
                                .id(5)
                                .amount(10000.0)
                                .type(WalletHistoryType.PENGELUARAN)
                                .verified(false)
                                .method("PayPal")
                                .detail("Pengeluaran")
                                .createdAt(new Date())
                                .user(pelangganUser)
                                .build();

                walletRepository.save(expense);

                ArrayList<WalletHistory> expectedResult = new ArrayList<>();
                expectedResult.add(expense);

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("role", pelangganUser.getRole());
                claims.put("id", pelangganUser.getId());
                claims.put("saldo", pelangganUser.getSaldo());
                claims.put("verified", pelangganUser.isVerified());
                claims.put("active", pelangganUser.isActive());
                claims.put("name", pelangganUser.getName());

                String token = jwtService.generateToken(claims, pelangganUser);
                String authorizationHeader = "Bearer " + token;

                when(walletService.getAllUserExpensesHistory(pelangganUser.getId())).thenReturn(expectedResult);

                MvcResult mvcResult = mvc.perform(get("/wallet/history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();

                String responseBody = mvcResult.getResponse().getContentAsString();
                assertNotNull(responseBody);
        }

}
