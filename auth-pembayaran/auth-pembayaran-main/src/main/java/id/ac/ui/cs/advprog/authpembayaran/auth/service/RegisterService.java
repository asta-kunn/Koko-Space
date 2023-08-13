package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.authpembayaran.auth.dto.RegisterResponse;

public interface RegisterService {
    RegisterResponse register(RegisterRequest request);
}
