package id.ac.ui.cs.advprog.authpembayaran.model.auth;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VerificationTokenTest {
    private VerificationToken verificationToken1;
    private VerificationToken verificationToken2;
    
    @BeforeEach
    void setUp() {
        String token = "testToken";
        String email = "test@example.com";
        verificationToken1 = new VerificationToken(token, email);
        verificationToken2 = new VerificationToken(token, email);
    }
    
    @Test
    void testEquals() {
        assertThat(verificationToken1).isEqualTo(verificationToken2);
    }
    
    @Test
    void testSetterMethods() {
        String newToken = "newToken";
        String newEmail = "new@example.com";
        
        verificationToken1.setToken(newToken);
        verificationToken1.setEmail(newEmail);
        
        assertThat(verificationToken1.getToken()).isEqualTo(newToken);
        assertThat(verificationToken1.getEmail()).isEqualTo(newEmail);
    }

}
