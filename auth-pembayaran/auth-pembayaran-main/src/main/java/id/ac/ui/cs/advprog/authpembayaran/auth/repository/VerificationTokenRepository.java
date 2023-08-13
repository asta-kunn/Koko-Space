package id.ac.ui.cs.advprog.authpembayaran.auth.repository;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);
    
    Optional<VerificationToken> findByEmail(String email);
}
