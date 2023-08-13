package id.ac.ui.cs.advprog.authpembayaran.service.auth;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.JwtServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl service;

    String expiredJwt;

    private User testUser;

    String jwt;
    private HashMap<String, Object> claims;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@test.com")
                .password("test")
                .name("test")
                .verified(false)
                .active(true)
                .role(Role.PELANGGAN)
                .build();

        claims = new HashMap<>();
        claims.put("role", testUser.getRole());
        claims.put("id", testUser.getId());
        claims.put("saldo", testUser.getSaldo());
        claims.put("verified", testUser.isVerified());
        claims.put("active", testUser.isActive());
        claims.put("name", testUser.getName());
        jwt = service.generateToken(claims, testUser);

    }

    @Test
    void whenSignInShouldReturnKey() {

        Key key = service.getSignInKey();
        assertNotNull(key);

    }

    @Test
    void whenGenerateTokenShouldReturnJwtToken() {

        String jwt = service.generateToken(claims, testUser);
        assertNotNull(jwt);

    }

    @Test
    void whenValidateTokenAndValidShouldReturnTrue() {

        boolean isValid = service.isTokenValid(jwt, testUser);
        assertTrue(isValid);

    }

    @Test
    void whenValidateTokenAndNotValidShouldFalse() {
        testUser.setEmail("diff_email@gmail.com");
        boolean isValid = service.isTokenValid(jwt, testUser);
        assertFalse(isValid);

    }

}
