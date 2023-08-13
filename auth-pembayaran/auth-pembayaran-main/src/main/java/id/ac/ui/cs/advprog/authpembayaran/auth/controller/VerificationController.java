package id.ac.ui.cs.advprog.authpembayaran.auth.controller;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerifyResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/verification")
    @PreAuthorize("hasAuthority('PENGELOLA') OR hasAuthority('PELANGGAN')")
    public ResponseEntity<VerificationResponse> verification(
            @RequestBody VerificationRequest request) {
        return ResponseEntity.ok(verificationService.sendVerificationToken(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verify(
            @RequestParam(required = false) String token) {
        return ResponseEntity.ok(verificationService.verifyToken(token));
    }
}
