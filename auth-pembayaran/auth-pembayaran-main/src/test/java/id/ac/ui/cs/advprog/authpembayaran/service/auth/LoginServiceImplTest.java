package id.ac.ui.cs.advprog.authpembayaran.service.auth;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ForgotPasswordRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ForgotPasswordResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.LoginRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.LoginResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.auth.helper.StringEncryptorDecryptor;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.EmailServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.JwtServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.LoginServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private EmailServiceImpl emailService;
    @InjectMocks
    private LoginServiceImpl service;

    @Mock
    private UserRepository repository;

    private User testUser;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtServiceImpl jwtService;

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
    void whenLoginWithWrongCredentialsShouldThrowException() {
        when(repository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(emailService.validate(testUser.getEmail())).thenReturn(true);
        var loginReq = new LoginRequest(testUser.getEmail(), "testwrong");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testUser.getEmail(),testUser.getPassword()))).thenReturn(null);
        assertThrows(IncorrectEmailOrPasswordException.class, () -> service.login(loginReq));

        verify(repository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void whenLoginWithEmailNotExistShouldThrowException() {

        when(emailService.validate("testnotexist@gmail.com")).thenReturn(true);
        var loginReq = new LoginRequest("testnotexist@gmail.com", "testpass");
        assertThrows(IncorrectEmailOrPasswordException.class, () ->
                service.login(loginReq));

        verify(repository, times(1)).findByEmail("testnotexist@gmail.com");
    }

    @Test
    void whenLoginWithEmptyEmailFieldShouldThrowEmailPasswordFieldRequiredException() {
        var loginReq = new LoginRequest(null, "wrong");
        assertThrows(EmailPasswordFieldRequiredException.class, () -> service.login(loginReq));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void whenLoginWithEmptyPasswordFieldShouldThrowEmailPasswordFieldRequiredException() {
        var loginReq = new LoginRequest("test@test.com", null);
        assertThrows(EmailPasswordFieldRequiredException.class,
                () -> service.login(loginReq));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void whenLoginWithCorrectEmailAndPasswordShouldReturnUserAndEmailMatched() {
        when(emailService.validate(testUser.getEmail())).thenReturn(true);
        when(repository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testUser.getEmail(),testUser.getPassword()))).thenReturn(null);
        LoginResponse loginResponse = service.login(new LoginRequest(testUser.getEmail(), testUser.getPassword()));
        assertNotNull(loginResponse);
        assertEquals(testUser.getEmail(),loginResponse.getEmail());
        verify(repository, times(2)).findByEmail(testUser.getEmail());
        verify(authenticationManager,times(1)).authenticate(new UsernamePasswordAuthenticationToken(testUser.getEmail(),testUser.getPassword()));

    }

    @Test
    void whenLoginWithCorrectEmailAndPasswordShouldReturnUserPengelolaAndEmailMatched() {
        testUser.setRole(Role.PENGELOLA);
        when(repository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(emailService.validate(testUser.getEmail())).thenReturn(true);
        when(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword())))
                .thenReturn(null);
        LoginResponse loginResponse = service.login(new LoginRequest(testUser.getEmail(), testUser.getPassword()));
        assertNotNull(loginResponse);
        assertEquals(testUser.getEmail(), loginResponse.getEmail());
        assertEquals("PENGELOLA", loginResponse.getRole());
        verify(repository, times(2)).findByEmail(testUser.getEmail());
    }

    @Test
    void whenLoginWithCorrectEmailAndPasswordShouldReturnUserPelangganAndEmailMatched() {
        testUser.setRole(Role.PELANGGAN);
        when(repository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(emailService.validate(testUser.getEmail())).thenReturn(true);

        when(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword())))
                .thenReturn(null);
        LoginResponse loginResponse = service.login(new LoginRequest(testUser.getEmail(), testUser.getPassword()));
        assertNotNull(loginResponse);
        assertEquals(testUser.getEmail(), loginResponse.getEmail());
        assertEquals("PELANGGAN", loginResponse.getRole());
        verify(repository, times(2)).findByEmail(testUser.getEmail());
    }

    @Test
    void whenLoginWithInvalidEmailFormatShouldThrowInvalidEmailInputException() {
        var loginReq = new LoginRequest("testtest.com", "Test");
        assertThrows(InvalidEmailInputException.class, () -> service.login(loginReq));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void whenForgotPasswordWithInvalidEmailFormatShouldThrowInvalidEmailInputException() {
        var forgotReq = new ForgotPasswordRequest("testtest.com");
        assertThrows(InvalidEmailInputException.class,
                () -> service.forgotPassword(forgotReq));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void whenForgotPasswordEmailNotFoundShouldThrowEmailNotFoundException(){
        when(emailService.validate("testtest@test.com")).thenReturn(true);
        var forgotReq = new ForgotPasswordRequest("testtest@test.com");
        assertThrows(EmailNotFoundException.class, () -> service.forgotPassword(forgotReq));
        verify(repository, times(1)).findByEmail("testtest@test.com");
    }

    @Test
    void whenForgotPasswordEmailFoundShouldReturnForgotPasswordResponse() {
        String url = "https://koko-space.vercel.app";
        when(repository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(emailService.validate(testUser.getEmail())).thenReturn(true);

        ForgotPasswordResponse forgotPasswordResponse = service
                .forgotPassword(new ForgotPasswordRequest(testUser.getEmail()));
        assertNotNull(forgotPasswordResponse);
        assertTrue(emailService.validate(testUser.getEmail()));
        String expectedResetLink = String.format("%s/auth/forgot-password/%s", url,
                StringEncryptorDecryptor.encrypt(testUser.getEmail() + ";" + forgotPasswordResponse.getSentAt()));

        assertEquals(expectedResetLink, forgotPasswordResponse.getResetLink());
        verify(repository, times(2)).findByEmail(testUser.getEmail());
        verify(emailService, times(1)).sendMail(anyString(), anyString(), anyString());
    }

    @Test
    void whenForgotPasswordEmailFoundButSendEmailFailedThrowSendEmailFailedException() {

        when(repository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(emailService.validate(testUser.getEmail())).thenReturn(true);

        doThrow(new SendEmailFailedException()).when(emailService).sendMail(anyString(), anyString(), anyString());
        assertTrue(emailService.validate(testUser.getEmail()));

        var forgotReq=new ForgotPasswordRequest(testUser.getEmail());
        assertThrows(SendEmailFailedException.class,
                () -> service.forgotPassword(forgotReq));

        verify(repository, times(2)).findByEmail(testUser.getEmail());
        verify(emailService, times(1)).sendMail(anyString(), anyString(), anyString());
    }

    @Test
    void whenForgotPasswordEmailEncryptFailedShouldReturnEncryptionErrorException() {
        assertNotNull(StringEncryptorDecryptor.encrypt("abcdefgh@gmail.com;2023-04-10T12:12:55.917599Z"));
    }

    @Test
    void whenForgotPasswordSuccessShouldReturnEncryptionErrorException() {
        assertNotNull(StringEncryptorDecryptor.encrypt("abcdefgh@gmail.com;2023-04-10T12:12:55.917599Z"));
    }

}
