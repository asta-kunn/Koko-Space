package id.ac.ui.cs.advprog.authpembayaran.service.auth;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerifyResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.VerificationToken;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.VerificationTokenRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.EmailService;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.VerificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VerificationServiceImpl verificationService;
    
    private User testUser;
    private VerificationToken testToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = User.builder()
                .email("johndoe@example.com")
                .password("test")
                .name("test")
                .verified(false)
                .active(true)
                .role(Role.PELANGGAN)
                .build();
        testToken = VerificationToken.builder()
                .email(testUser.getEmail())
                .token("test")
                .build();
    }
    
    @Test
    void whenSendVerificationTokenButUserNotFoundShouldThrowException() {
        VerificationRequest request = new VerificationRequest(1);
        assertThrows(UserNotFoundException.class, () -> verificationService.sendVerificationToken(request));
        verify(userRepository, times(1)).findById(anyInt());
        verify(tokenRepository, never()).findByEmail(anyString());
        verify(tokenRepository, never()).delete(any());
        verify(tokenRepository, never()).save(any(VerificationToken.class));
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString());
    }
    
    @Test
    void whenSendVerificationButUserAlreadyVerifiedShouldThrowException() {
        testUser.setVerified(true);
        VerificationRequest request = new VerificationRequest(1);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        assertThrows(UserAlreadyVerifiedException.class, () -> verificationService.sendVerificationToken(request));
        verify(userRepository, times(1)).findById(anyInt());
        verify(tokenRepository, never()).findByEmail(anyString());
        verify(tokenRepository, never()).delete(any());
        verify(tokenRepository, never()).save(any(VerificationToken.class));
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString());
    }
    
    @Test
    void whenSendVerificationButInvalidEmailShouldThrowException() {
        testUser.setEmail("test");
        VerificationRequest request = new VerificationRequest(1);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.of(testToken));
        when(emailService.validate(testUser.getEmail())).thenReturn(false);
        // Act and Assert
        assertThrows(InvalidEmailInputException.class, () -> verificationService.sendVerificationToken(request));

        // Verify
        verify(userRepository, times(1)).findById(anyInt());
        verify(tokenRepository, times(1)).findByEmail(anyString());
        verify(tokenRepository, times(1)).delete(any(VerificationToken.class));
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString());
    }
    
    @Test
    void whenSendVerificationButEmailNotSentShouldThrowException() {
        testUser.setEmail("test@test.com");
        VerificationRequest request = new VerificationRequest(1);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.of(testToken));
        when(emailService.validate(testUser.getEmail())).thenReturn(true);
        doThrow(SendEmailFailedException.class).when(emailService).sendMail(anyString(), anyString(), anyString());
        // Act and Assert
        assertThrows(SendEmailFailedException.class, () -> verificationService.sendVerificationToken(request));
        
        // Verify
        verify(userRepository, times(1)).findById(anyInt());
        verify(tokenRepository, times(1)).findByEmail(anyString());
        verify(tokenRepository, times(1)).delete(any(VerificationToken.class));
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
        verify(emailService, times(1)).sendMail(anyString(), anyString(), anyString());
    }
    
    @Test
    void whenSendVerificationAndSucceed() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(emailService.validate(testUser.getEmail())).thenReturn(true);
        
        // Act and Assert
        VerificationResponse vR = verificationService.sendVerificationToken(new VerificationRequest(1));
        assertNotNull(vR);
        
        // Verify
        verify(userRepository, times(1)).findById(anyInt());
        verify(tokenRepository, times(1)).findByEmail(anyString());
        verify(tokenRepository, never()).delete(any(VerificationToken.class));
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
        verify(emailService, times(1)).sendMail(anyString(), anyString(), anyString());
    }
    
    @Test
    void whenVerifyButTokenNotFoundShouldThrowException() {
        when(tokenRepository.findByToken(anyString())).thenThrow(InvalidTokenException.class);
        assertThrows(InvalidTokenException.class, () -> verificationService.verifyToken("test"));
        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void whenVerifyButUserNotFoundShouldThrowException() {
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(testToken));
        when(userRepository.findByEmail(anyString())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> verificationService.verifyToken("test"));
        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void whenVerifyButUserAlreadyVerifiedShouldThrowException() {
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(testToken));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        testUser.setVerified(true);
        assertThrows(UserAlreadyVerifiedException.class, () -> verificationService.verifyToken("test"));
        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void whenVerifyButTokenExpiredShouldThrowException() {
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(testToken));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        testToken.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        assertThrows(VerificationTokenExpiredException.class, () -> verificationService.verifyToken("test"));
        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void whenVerifyAndSucceed() {
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(testToken));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        testToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000));
        VerifyResponse vR = verificationService.verifyToken("test");
        assertNotNull(vR);
        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
}
