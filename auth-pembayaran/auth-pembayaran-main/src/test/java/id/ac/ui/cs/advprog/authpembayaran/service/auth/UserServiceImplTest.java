package id.ac.ui.cs.advprog.authpembayaran.service.auth;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ChangePasswordRequest;

import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.ResetPasswordTokenExpiredException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.SecretTokenNewPasswordFieldRequiredException;
import id.ac.ui.cs.advprog.authpembayaran.auth.helper.StringEncryptorDecryptor;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private User testUser;

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
    }

    @Test
    void whenChangePasswordNewPasswordFieldEmptyShouldThrowIncompleteField() {
        var changePassReq = new ChangePasswordRequest("a", null);
        assertThrows(SecretTokenNewPasswordFieldRequiredException.class,
                () -> service.changePassword(changePassReq));
        verify(userRepository, never()).findByEmail(anyString());

    }

    @Test
    void whenChangePasswordSecretTokenFieldEmptyShouldThrowIncompleteField() {
        var changePassReq = new ChangePasswordRequest(null, "a");
        assertThrows(SecretTokenNewPasswordFieldRequiredException.class,
                () -> service.changePassword(changePassReq));
        verify(userRepository, never()).findByEmail(anyString());

    }

    @Test
    void whenChangePaswordTokenExpiredShouldThrowResetPasswordTokenExpiredException(){
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
       String encString = StringEncryptorDecryptor.encrypt("test@test.com;"+ZonedDateTime.now().minusMinutes(10));
       var changePassReq = new ChangePasswordRequest(encString,"newpassword");
       assertThrows(ResetPasswordTokenExpiredException.class,() -> service.changePassword(changePassReq));
    }

    @Test
    void whenChangePaswordShouldReturnStringSuccess(){

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        String encString = StringEncryptorDecryptor.encrypt("test@test.com;"+ZonedDateTime.now());
        String response = service.changePassword(new ChangePasswordRequest(encString,"newpassword"));

        assertEquals("Password has been changed",response);

        verify(userRepository,times(1)).save(testUser);

    }
}
