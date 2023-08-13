package id.ac.ui.cs.advprog.authpembayaran.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import id.ac.ui.cs.advprog.authpembayaran.auth.service.EmailServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl service;

    @Value("${spring.mail.username}")
    private String sender;

    @Mock
    private SimpleMailMessage mailMessage;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo("test@test.gmail.com");
        mailMessage.setSubject("test");
        mailMessage.setText("test");
    }

    @Test
    void whenGivenEmailPatternInvalidReturnFalse() {
        assertFalse(service.validate("testtest.test.com"));
    }

    @Test
    void whenGivenEmailPatternValidReturnTrue() {
        assertTrue(service.validate("testtest@test.com"));
    }

    @Test
    void whenSendMailDoSendMail() {
        assertDoesNotThrow(() -> {
            service.sendMail("test@testgmail.com", "test", "test");
        });

        mailMessage.setFrom("test@gmail.com");
        mailMessage.setTo("test@testgmail.com");
        mailMessage.setSubject("test");
        mailMessage.setText("test");

    }
}
