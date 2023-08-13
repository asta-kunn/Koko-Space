package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerificationResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.VerifyResponse;

public interface VerificationService {
    VerificationResponse sendVerificationToken(VerificationRequest request);

    VerifyResponse verifyToken(String request);
}
