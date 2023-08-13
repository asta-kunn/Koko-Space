package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ForgotPasswordRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ForgotPasswordResponse;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.LoginRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
}
