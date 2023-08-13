package id.ac.ui.cs.advprog.authpembayaran.service.auth;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.InvalidInputFormatPasswordException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UsernameEmailPasswordFieldRequiredException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.InvalidInputFormatEmailException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.RegisterServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {
    @InjectMocks
    private RegisterServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void whenRegisterWithValidInputReturnsRegisterResponse() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("Password1!");

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        RegisterResponse response = service.register(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertFalse(response.getIsVerified());

        verify(repository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void whenRegisterWithEmptyFieldShouldThrowException() {
        RegisterRequest request = new RegisterRequest();
        assertThrows(UsernameEmailPasswordFieldRequiredException.class,
                () -> service.register(request));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void whenRegisterWithEmptyPasswordFieldShouldThrowException() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john1@example.com");

        assertThrows(UsernameEmailPasswordFieldRequiredException.class, () -> {
            service.register(request);
        });
    }

    @Test
    void whenRegisterWithEmptyEmailFieldShouldThrowException() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setPassword("Password1!");

        assertThrows(UsernameEmailPasswordFieldRequiredException.class, () -> {
            service.register(request);
        });
    }

    @Test
    void whenRegisterWithEmptyNameFieldShouldThrowException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john1@example.com");
        request.setPassword("Password1!");

        assertThrows(UsernameEmailPasswordFieldRequiredException.class, () -> {
            service.register(request);
        });
    }

    @Test
    void whenRegisterWithExistingEmailShouldThrowUserAlreadyExistException() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("Password1!");

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> {
            service.register(request);
        });
    }

    @Test
    void whenRegisterWithInvalidInputFormatPasswordThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("weakpassword");

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidInputFormatPasswordException.class, () -> {
            service.register(request);
        });
    }
    @Test
    void whenRegisterWithInvalidInputFormatEmailThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@com");
        request.setPassword("Weakpassword1!");

        assertThrows(InvalidInputFormatEmailException.class, () -> {
            service.register(request);
        });
    }

}
