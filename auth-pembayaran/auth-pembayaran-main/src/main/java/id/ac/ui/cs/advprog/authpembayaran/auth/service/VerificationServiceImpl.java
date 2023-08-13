package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerifyResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.VerificationToken;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VerificationServiceImpl implements VerificationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final VerificationTokenRepository tokenRepository;
    @Autowired
    private final EmailService emailService;

    @Override
    public VerificationResponse sendVerificationToken(VerificationRequest request) {

        User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);

        if (user.isVerified()) {
            throw new UserAlreadyVerifiedException();
        }

        tokenRepository.findByEmail(user.getEmail()).ifPresent(tokenRepository::delete);

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken(token, user.getEmail());

        tokenRepository.save(verificationToken);

        var url = "https://koko-space.vercel.app/auth/verify?token=" + token;

        var text = "Dear " + user.getName() + ",\n" +
                "\n" +
                "Thank you for creating an account with Koko Space. To ensure the security of your account and to prevent unauthorized access, we require all our users to verify their email addresses.\n" +
                "\n" +
                "Please click on the following link to verify your email address: " + url + "\n" +
                "\n" +
                "Thank you for choosing Koko Space.\n" +
                "\n" +
                "Best regards,\n" +
                "Koko Space Team";
        
        if (!emailService.validate(user.getEmail())) {
            throw new InvalidEmailInputException();
        }
        
        try {
            emailService.sendMail(user.getEmail(), "Account Verification", text);
        } catch (Exception e) {
            throw new SendEmailFailedException();
        }
        return new VerificationResponse("Verification email sent");
    }

    @Override
    public VerifyResponse verifyToken(String request) {
        VerificationToken token = tokenRepository.findByToken(request).orElseThrow(InvalidTokenException::new);

        User user = userRepository.findByEmail(token.getEmail()).orElseThrow(UserNotFoundException::new);

        if (user.setVerified()) {
            throw new UserAlreadyVerifiedException();
        }

        Calendar cal = Calendar.getInstance();

        if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new VerificationTokenExpiredException();
        }

        user.setVerified(true);

        userRepository.save(user);

        return new VerifyResponse("Account verified");
    }
}
