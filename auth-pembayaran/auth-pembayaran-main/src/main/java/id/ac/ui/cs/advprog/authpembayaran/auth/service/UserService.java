package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.dto.ChangePasswordRequest;

public interface UserService {

    public String changePassword(ChangePasswordRequest request);

}
