package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ForgotPasswordRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ForgotPasswordResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.LoginRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.LoginResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.auth.helper.StringEncryptorDecryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest request) {
        HashMap<String, Object> claims = new HashMap<>();

        if (request.getEmail() == null || request.getPassword() == null) {

            throw new EmailPasswordFieldRequiredException();
        }

        if (!emailService.validate(request.getEmail())) {
            throw new InvalidEmailInputException();
        }

        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new IncorrectEmailOrPasswordException();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (Exception e) {
            throw new IncorrectEmailOrPasswordException();
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        claims.put("role", user.getRole());
        claims.put("id", user.getId());
        claims.put("saldo", user.getSaldo());
        claims.put("verified", user.isVerified());
        claims.put("active", user.isActive());
        claims.put("name", user.getName());

        var jwtToken = jwtService.generateToken(claims, user);

        return LoginResponse.builder().token(jwtToken).name(user.getName()).saldo(user.getSaldo())
                .email(user.getEmail())
                .verified(user.getVerified()).role(user.getRole()).build();
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {

        String url = "https://koko-space.vercel.app";

        if (!emailService.validate(request.getEmail())) {
            throw new InvalidEmailInputException();
        }

        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new EmailNotFoundException();
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Z"));

        var secret = StringEncryptorDecryptor.encrypt(user.getEmail() + ";" + date);
        url = String.format("%s/auth/forgot-password/%s", url, secret);

        try {
            emailService.sendMail(request.getEmail(), "Password Reset Request",
                    "If you requested a password reset, use the confirmation code below to complete the process. If you didn't make this request, ignore this email.\n"
                            + url);
        } catch (Exception e) {
            throw new SendEmailFailedException();
        }

        return ForgotPasswordResponse.builder().email(request.getEmail()).resetLink(url).sentAt(date).build();
    }
}
