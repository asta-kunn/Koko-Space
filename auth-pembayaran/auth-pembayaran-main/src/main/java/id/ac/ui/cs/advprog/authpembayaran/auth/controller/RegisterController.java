package id.ac.ui.cs.advprog.authpembayaran.auth.controller;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.RegisterService;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(registerService.register(request));
    }
}