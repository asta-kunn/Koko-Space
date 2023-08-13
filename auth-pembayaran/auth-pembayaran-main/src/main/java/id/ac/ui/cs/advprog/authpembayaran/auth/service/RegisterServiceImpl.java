package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterResponse;

import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.InvalidInputFormatPasswordException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UsernameEmailPasswordFieldRequiredException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.InvalidInputFormatEmailException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserAlreadyExistException;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String EMAILREGEX = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
            + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
    private static final String PASSWORDREGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null
        || request.getName().equals("") || request.getEmail().equals("") || request.getPassword().equals("")){
            throw new UsernameEmailPasswordFieldRequiredException();
        }
        if (!Pattern.compile(EMAILREGEX).matcher(request.getEmail()).matches()) {
            throw new InvalidInputFormatEmailException();
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        if (!Pattern.compile(PASSWORDREGEX).matcher(request.getPassword()).matches()) {
            throw new InvalidInputFormatPasswordException();
        }

        var user = User.builder()
                .name(request.getName())
                .active(true)
                .email(request.getEmail())
                .verified(false)
                .password(passwordEncoder.encode(request.getPassword()))
                .saldo(0.0)
                .role(Role.PELANGGAN)
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .isVerified(user.getVerified())
                .build();
    }

}
