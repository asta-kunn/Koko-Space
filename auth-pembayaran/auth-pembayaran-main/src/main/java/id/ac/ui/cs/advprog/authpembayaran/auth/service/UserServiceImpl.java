package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.ResetPasswordTokenExpiredException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.SecretTokenNewPasswordFieldRequiredException;
import id.ac.ui.cs.advprog.authpembayaran.auth.helper.StringEncryptorDecryptor;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String changePassword(ChangePasswordRequest request) {

        if (request.getSecretToken() == null || request.getNewPassword() == null) {
            throw new SecretTokenNewPasswordFieldRequiredException();
        }

        String[] decryptedStr = StringEncryptorDecryptor.decrypt(request.getSecretToken()).split(";");

        var user = userRepository.findByEmail(decryptedStr[0]).orElseThrow();

        if (MINUTES.between(ZonedDateTime.parse(decryptedStr[1]), ZonedDateTime.now()) >= 5) {
            throw new ResetPasswordTokenExpiredException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password has been changed";
    }

}
